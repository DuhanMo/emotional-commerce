package com.loopers.domain.order

import com.loopers.domain.order.OrderInfo.OrderLineInfo
import com.loopers.domain.product.ProductSummary
import com.loopers.infrastructure.order.OrderJpaRepository
import com.loopers.infrastructure.product.ProductJpaRepository
import com.loopers.infrastructure.product.ProductSummaryJpaRepository
import com.loopers.support.fixture.createOrderCommand
import com.loopers.support.fixture.createProduct
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class OrderServiceIGTest(
    private val orderService: OrderService,
    private val orderJpaRepository: OrderJpaRepository,
    private val productJpaRepository: ProductJpaRepository,
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
) : IntegrationSpec({
    Given("유효한 주문 생성 명령이 주어진 경우") {
        val product1 = productJpaRepository.save(createProduct(name = "상품1", stock = 10))
        val product2 = productJpaRepository.save(createProduct(name = "상품2", stock = 10))
        productSummaryJpaRepository.save(ProductSummary(productId = product1.id))
        productSummaryJpaRepository.save(ProductSummary(productId = product2.id))

        val command = createOrderCommand(
            userId = 1L,
            orderLines = listOf(
                OrderLineInfo(productId = product1.id, quantity = 2, unitPrice = 10_000),
                OrderLineInfo(productId = product2.id, quantity = 1, unitPrice = 5_000),
            ),
        )

        When("주문을 생성하면") {
            val result = orderService.createOrder(command)

            Then("주문이 성공적으로 생성되고 데이터베이스에 저장된다") {
                // 주문 객체 검증
                result shouldNotBe null
                result.id shouldNotBe 0L
                result.userId shouldBe 1L
                result.status shouldBe OrderStatus.PENDING
                result.orderLineInfos.size shouldBe 2
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
        val product = productJpaRepository.save(createProduct(name = "상품1", stock = 5))
        productSummaryJpaRepository.save(ProductSummary(productId = product.id))

        val command = createOrderCommand(
            userId = 99L,
            orderLines = listOf(
                OrderLineInfo(productId = product.id, quantity = 3, unitPrice = 10_000),
            ),
        )

        When("주문을 생성하면") {
            val result = orderService.createOrder(command)

            Then("주문이 올바르게 생성된다") {
                result.userId shouldBe 99L
                result.status shouldBe OrderStatus.PENDING
                result.orderLineInfos.size shouldBe 1
                result.orderLineInfos[0].productId shouldBe product.id
                result.orderLineInfos[0].quantity shouldBe 3
                result.orderLineInfos[0].unitPrice shouldBe 10_000
                result.orderLineInfos[0].lineAmount shouldBe 30_000
                result.totalAmount shouldBe 30_000
            }
        }
    }

    Given("여러 상품으로 주문을 생성하는 경우") {
        val product1 = productJpaRepository.save(createProduct(name = "상품1", stock = 20))
        val product2 = productJpaRepository.save(createProduct(name = "상품2", stock = 20))
        val product3 = productJpaRepository.save(createProduct(name = "상품3", stock = 20))
        productSummaryJpaRepository.save(ProductSummary(productId = product1.id))
        productSummaryJpaRepository.save(ProductSummary(productId = product2.id))
        productSummaryJpaRepository.save(ProductSummary(productId = product3.id))

        val command = createOrderCommand(
            userId = 2L,
            orderLines = listOf(
                OrderLineInfo(productId = product1.id, quantity = 1, unitPrice = 20000),
                OrderLineInfo(productId = product2.id, quantity = 2, unitPrice = 15000),
                OrderLineInfo(productId = product3.id, quantity = 3, unitPrice = 10000),
            ),
        )

        When("주문을 생성하면") {
            val result = orderService.createOrder(command)

            Then("모든 상품이 올바르게 주문에 포함된다") {
                result.orderLineInfos.size shouldBe 3
                result.totalAmount shouldBe 80000 // 20000 + 30000 + 30000

                val orderLine1 = result.orderLineInfos.find { it.productId == product1.id }!!
                orderLine1.lineAmount shouldBe 20000

                val orderLine2 = result.orderLineInfos.find { it.productId == product2.id }!!
                orderLine2.lineAmount shouldBe 30000

                val orderLine3 = result.orderLineInfos.find { it.productId == product3.id }!!
                orderLine3.lineAmount shouldBe 30000
            }
        }
    }
})
