// package com.loopers.domain.payment
//
// import com.loopers.domain.point.Point
// import com.loopers.infrastructure.order.OrderJpaRepository
// import com.loopers.infrastructure.point.PointJpaRepository
// import com.loopers.infrastructure.product.ProductJpaRepository
// import com.loopers.infrastructure.product.ProductSummaryJpaRepository
// import com.loopers.support.fixture.createOrder
// import com.loopers.support.fixture.createOrderLine
// import com.loopers.support.fixture.createProduct
// import com.loopers.support.fixture.createProductSummary
// import com.loopers.support.fixture.createUser
// import com.loopers.support.tests.IntegrationSpec
// import io.kotest.matchers.shouldBe
// import io.kotest.matchers.string.shouldContain
// import org.junit.jupiter.api.assertThrows
// import org.springframework.data.repository.findByIdOrNull
//
// class PaymentServiceIGTest(
//    private val paymentService: PaymentService,
//    private val productJpaRepository: ProductJpaRepository,
//    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
//    private val pointJpaRepository: PointJpaRepository,
//    private val orderJpaRepository: OrderJpaRepository,
// ) : IntegrationSpec({
//    Given("포인트가 부족한 결제 요청인 경우") {
//        val product = productJpaRepository.save(createProduct())
//        productSummaryJpaRepository.save(createProductSummary(productId = product.id))
//
//        val user = createUser()
//        val point = Point(userId = user.id, amount = 5_000L)
//        val order = createOrder(userId = user.id)
//        // 포인트를 초과한 주문상품 가격
//        order.addOrderLines(listOf(createOrderLine(productId = product.id, unitPrice = 10_000L)))
//
//        orderJpaRepository.save(order)
//        pointJpaRepository.save(point)
//
//        When("결제를 시도하면") {
//            Then("예외가 발생하고 상태가 변경되지 않는다") {
//                val exception = assertThrows<IllegalArgumentException> { paymentService.payRequest(user, order) }
//                exception.message shouldContain "포인트가 부족합니다"
//
//                val foundOrder = orderJpaRepository.findByIdOrNull(order.id)!!
//                foundOrder.status shouldBe OrderStatus.PENDING
//            }
//        }
//    }
//
//    Given("충분한 포인트가 있는 정상적인 결제 요청") {
//        val product = productJpaRepository.save(createProduct(stock = 100))
//        productSummaryJpaRepository.save(createProductSummary(productId = product.id))
//        val user = createUser()
//        // 충분한 포인트
//        val point = Point(userId = user.id, amount = 100_000L)
//        val order = createOrder(userId = user.id)
//        order.addOrderLines(
//            listOf(
//                createOrderLine(productId = product.id, unitPrice = 10_000L, quantity = 1),
//                createOrderLine(productId = product.id, unitPrice = 10_000L, quantity = 2),
//            ),
//        )
//        orderJpaRepository.save(order)
//        pointJpaRepository.save(point)
//
//        When("결제를 시도하면") {
//            paymentService.payRequest(user, order)
//
//            Then("주문 상태가 PAID로 변경되고 포인트가 차감된다") {
//                val foundOrder = orderJpaRepository.findByIdOrNull(order.id)!!
//                foundOrder.status shouldBe OrderStatus.PAID
//
//                val foundPoint = pointJpaRepository.findByIdOrNull(point.id)!!
//                foundPoint.amount shouldBe 70_000L
//            }
//        }
//    }
// })
