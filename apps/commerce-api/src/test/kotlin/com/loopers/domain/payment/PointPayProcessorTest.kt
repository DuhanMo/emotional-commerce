package com.loopers.domain.payment

import com.loopers.domain.point.Point
import com.loopers.infrastructure.point.PointHistoryJpaRepository
import com.loopers.infrastructure.point.PointJpaRepository
import com.loopers.support.fixture.createOrder
import com.loopers.support.fixture.createOrderLine
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull

class PointPayProcessorTest(
    private val pointPayProcessor: PointPayProcessor,
    private val pointJpaRepository: PointJpaRepository,
    private val pointHistoryJpaRepository: PointHistoryJpaRepository,
) : IntegrationSpec({
    Given("유저가 동시에 포인트 차감하는 경우") {
        val user = createUser(id = 1L)
        val order = createOrder(userId = user.id)
        order.addOrderLines(listOf(createOrderLine(quantity = 1, unitPrice = 10_000L)))
        val point = pointJpaRepository.save(Point(user.id, amount = 50_000L))

        When("포인트 차감하면") {
            val results = (1..2).map {
                async(Dispatchers.IO) {
                    runCatching {
                        pointPayProcessor.process(user, order)
                    }
                }
            }.awaitAll()

            Then("포인트가 모두 정상 차감되어야 한다") {
                val foundPoint = pointJpaRepository.findByIdOrNull(point.id)!!

                foundPoint.amount shouldBe 30_000L
                pointHistoryJpaRepository.findAll().count() shouldBe 2

                results.count { it.isSuccess } shouldBe 2
                results.count { it.isFailure } shouldBe 0
            }
        }
    }

    Given("유저 포인트를 초과하는 경우") {
        val user = createUser(id = 1L)
        val order = createOrder(userId = user.id)
        order.addOrderLines(listOf(createOrderLine(quantity = 1, unitPrice = 10_000L)))
        pointJpaRepository.save(Point(user.id, amount = 1_500L))

        When("포인트 차감하면") {
            Then("예외발생한다") {
                assertThrows<IllegalArgumentException> {
                    pointPayProcessor.process(user, order)
                }
            }
        }
    }
})
