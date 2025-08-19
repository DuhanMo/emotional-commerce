package com.loopers.domain.payment

import com.loopers.domain.support.Money
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.string.shouldContain
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.assertThrows

class PaymentServiceTest : BehaviorSpec({
    val payProcessor = mockk<PayProcessor>()
    val service = PaymentService(listOf(payProcessor))

    Given("지원하지 않는 결제방법인 경우") {
        val command = PayRequestCommand(
            userId = 1L,
            orderId = 1L,
            paymentMethod = PaymentMethod.CARD,
            amount = Money(5_000),
        )
        every { payProcessor.support() } returns PaymentMethod.POINT

        When("결제를 시도하면") {
            Then("예외가 발생한다") {
                val exception = assertThrows<IllegalArgumentException> { service.payRequest(command) }
                exception.message shouldContain "지원하지 않는 결제 방법입니다"
            }
        }
    }

    afterTest {
        clearAllMocks()
    }
})
