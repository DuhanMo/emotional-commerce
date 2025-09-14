package com.loopers.application.payment

import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.payment.PaymentRepository
import com.loopers.domain.point.Point
import com.loopers.domain.point.PointHistoryRepository
import com.loopers.domain.point.PointRepository
import com.loopers.domain.support.Money
import com.loopers.support.fixture.TEST_IDEMPOTENCY_KEY
import com.loopers.support.fixture.createPayment
import com.loopers.support.fixture.createPoint
import com.loopers.support.fixture.createPointHistory
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class PointPaymentProcessorTest : DescribeSpec({
    val pointRepository = mockk<PointRepository>()
    val pointHistoryRepository = mockk<PointHistoryRepository>()
    val paymentRepository = mockk<PaymentRepository>()
    val underTest = PointPaymentProcessor(pointRepository, pointHistoryRepository, paymentRepository)

    describe("포인트 결제") {
        context("유저의 포인트가 존재하지 않는 경우") {
            it("예외 발생한다") {
                // given
                every { pointRepository.getByUserIdWithLock(any()) } throws NoSuchElementException()

                // when & then
                shouldThrow<NoSuchElementException> {
                    underTest.process(createCommand())
                }
            }
        }

        context("유저의 포인트가 존재하는 경우") {
            it("포인트가 차감된다") {
                // given
                val point = createPoint(amount = Money(20_000))
                stubbingSuccessCase(point, pointRepository, pointHistoryRepository, paymentRepository)

                // when
                underTest.process(createCommand(Money(500)))

                // then
                point.amount shouldBe Money(19_500)
            }

            it("포인트 히스토리가 저장된다") {
                // given
                val point = createPoint(amount = Money(20_000))
                stubbingSuccessCase(point, pointRepository, pointHistoryRepository, paymentRepository)

                // when
                underTest.process(createCommand(Money(500)))

                // then
                verify(exactly = 1) { pointHistoryRepository.save(any()) }
            }

            it("결제가 저장된다") {
                // given
                val point = createPoint(amount = Money(20_000))
                stubbingSuccessCase(point, pointRepository, pointHistoryRepository, paymentRepository)

                // when
                underTest.process(createCommand(Money(500)))

                // then
                verify(exactly = 1) { paymentRepository.save(any()) }
            }
        }

        context("유저의 포인트가 충분하지 않은 경우") {
            it("예외가 발생한다") {
                // given
                val insufficientPoint = createPoint(amount = Money(200))
                every { pointRepository.getByUserIdWithLock(any()) } returns insufficientPoint

                // when & then
                shouldThrow<IllegalArgumentException> {
                    underTest.process(createCommand(Money(500)))
                }
            }
        }
    }

    afterTest {
        clearAllMocks()
    }
})

private fun stubbingSuccessCase(
    point: Point,
    pointRepository: PointRepository,
    pointHistoryRepository: PointHistoryRepository,
    paymentRepository: PaymentRepository,
) {
    every { pointRepository.getByUserIdWithLock(any()) } returns point
    every { pointRepository.save(any()) } returns point
    every { pointHistoryRepository.save(any()) } returns createPointHistory()
    every { paymentRepository.save(any()) } returns createPayment()
}

private fun createCommand(amount: Money = Money(1000)): RequestPaymentCommand = RequestPaymentCommand(
    idempotencyKey = TEST_IDEMPOTENCY_KEY,
    userId = 1L,
    orderId = 1L,
    amount = amount,
    cardType = null,
    cardNumber = null,
    paymentMethod = PaymentMethod.POINT,
)
