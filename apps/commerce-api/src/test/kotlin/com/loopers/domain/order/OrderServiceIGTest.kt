package com.loopers.domain.order

import com.loopers.infrastructure.order.OrderJpaRepository
import com.loopers.support.fixture.createOrderCommand
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class OrderServiceIGTest(
    private val orderService: OrderService,
    private val orderJpaRepository: OrderJpaRepository,
) : IntegrationSpec({
    Given("유효한 주문 생성 명령이 주어진 경우") {
        val command = createOrderCommand(
            userId = 1L,
            orderItems = listOf(
                CreateOrderCommand.OrderItem(productId = 1L, quantity = 2, unitPrice = 10000),
                CreateOrderCommand.OrderItem(productId = 2L, quantity = 1, unitPrice = 5000)
            )
        )

        When("주문을 생성하면") {
            val result = orderService.createOrder(command)

            Then("주문이 성공적으로 생성되고 데이터베이스에 저장된다") {
                // 주문 객체 검증
                result shouldNotBe null
                result.id shouldNotBe 0L
                result.userId shouldBe 1L
                result.status shouldBe OrderStatus.PENDING
                result.orderLines.size shouldBe 2
                result.totalAmount shouldBe 25000

                // 데이터베이스 저장 검증
                val savedOrder = orderJpaRepository.findByIdWithOrderLines(result.id)!!
                savedOrder shouldNotBe null
                savedOrder.userId shouldBe 1L
                savedOrder.status shouldBe OrderStatus.PENDING
                savedOrder.orderLines.size shouldBe 2
                savedOrder.totalAmount shouldBe 25000
            }
        }
    }

    Given("단일 상품으로 주문을 생성하는 경우") {
        val command = createOrderCommand(
            userId = 99L,
            orderItems = listOf(
                CreateOrderCommand.OrderItem(productId = 10L, quantity = 3, unitPrice = 15000)
            )
        )

        When("주문을 생성하면") {
            val result = orderService.createOrder(command)

            Then("주문이 올바르게 생성된다") {
                result.userId shouldBe 99L
                result.status shouldBe OrderStatus.PENDING
                result.orderLines.size shouldBe 1
                result.orderLines[0].productId shouldBe 10L
                result.orderLines[0].quantity shouldBe 3
                result.orderLines[0].unitPrice shouldBe 15000
                result.orderLines[0].lineAmount shouldBe 45000
                result.totalAmount shouldBe 45000
            }
        }
    }

    Given("여러 상품으로 주문을 생성하는 경우") {
        val command = createOrderCommand(
            userId = 2L,
            orderItems = listOf(
                CreateOrderCommand.OrderItem(productId = 1L, quantity = 1, unitPrice = 20000),
                CreateOrderCommand.OrderItem(productId = 2L, quantity = 2, unitPrice = 15000),
                CreateOrderCommand.OrderItem(productId = 3L, quantity = 3, unitPrice = 10000)
            )
        )

        When("주문을 생성하면") {
            val result = orderService.createOrder(command)

            Then("모든 상품이 올바르게 주문에 포함된다") {
                result.orderLines.size shouldBe 3
                result.totalAmount shouldBe 80000 // 20000 + 30000 + 30000

                val orderLine1 = result.orderLines.find { it.productId == 1L }!!
                orderLine1.lineAmount shouldBe 20000

                val orderLine2 = result.orderLines.find { it.productId == 2L }!!
                orderLine2.lineAmount shouldBe 30000

                val orderLine3 = result.orderLines.find { it.productId == 3L }!!
                orderLine3.lineAmount shouldBe 30000
            }
        }
    }
})