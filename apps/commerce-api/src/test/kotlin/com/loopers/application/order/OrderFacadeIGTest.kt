package com.loopers.application.order

import com.loopers.application.order.PlaceOrderInput.AddressInput
import com.loopers.domain.order.PayMethod
import com.loopers.domain.product.ProductSummary
import com.loopers.domain.user.LoginId
import com.loopers.infrastructure.order.OrderJpaRepository
import com.loopers.infrastructure.product.ProductJpaRepository
import com.loopers.infrastructure.product.ProductSummaryJpaRepository
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.support.fixture.createProduct
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class OrderFacadeIGTest(
    private val orderFacade: OrderFacade,
    private val userJpaRepository: UserJpaRepository,
    private val productJpaRepository: ProductJpaRepository,
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
    private val orderJpaRepository: OrderJpaRepository,
) : IntegrationSpec({

    Given("사용자와 상품이 존재하는 경우") {
        val user = userJpaRepository.save(createUser(LoginId("testuser1")))
        val product1 = productJpaRepository.save(createProduct(name = "상품1", price = 10000))
        val product2 = productJpaRepository.save(createProduct(name = "상품2", price = 5000))
        productSummaryJpaRepository.save(ProductSummary(productId = product1.id, likeCount = 0L))
        productSummaryJpaRepository.save(ProductSummary(productId = product2.id, likeCount = 0L))

        When("주문을 생성하면") {
            val input = PlaceOrderInput(
                loginId = LoginId("testuser1"),
                address = AddressInput(
                    street = "강남대로 123",
                    city = "서울시",
                    zipCode = "12345",
                    detailAddress = "테스트 상세주소",
                ),
                payMethod = PayMethod.POINT,
                orderItems = listOf(
                    PlaceOrderInput.OrderLineInput(
                        productId = product1.id,
                        quantity = 2,
                        unitPrice = 10000,
                    ),
                    PlaceOrderInput.OrderLineInput(
                        productId = product2.id,
                        quantity = 1,
                        unitPrice = 5000,
                    ),
                ),
            )

            val result = orderFacade.placeOrder(input)

            Then("주문이 정상적으로 생성되고 응답이 반환된다") {
                result.id shouldNotBe null
                result.orderLineInfos.size shouldBe 2
                result.totalAmount shouldBe 25000L
                result.deliveryAddress.street shouldBe "강남대로 123"
                result.deliveryAddress.city shouldBe "서울시"
                result.payMethod shouldBe PayMethod.POINT

                val savedOrder = orderJpaRepository.findByIdWithOrderLines(result.id)!!
                savedOrder.userId shouldBe user.id
                savedOrder.totalAmount shouldBe 25000L
                savedOrder.orderLines.size shouldBe 2
            }
        }
    }

    Given("여러 주문을 연속으로 생성하는 경우") {
        val user1 = userJpaRepository.save(createUser(LoginId("user1")))
        val user2 = userJpaRepository.save(createUser(LoginId("user2")))
        val product = productJpaRepository.save(createProduct(name = "공통상품", price = 15000))
        productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 0L))

        When("서로 다른 사용자가 주문을 생성하면") {
            val input1 = PlaceOrderInput(
                loginId = LoginId("user1"),
                address = AddressInput(
                    street = "테헤란로 1",
                    city = "서울시",
                    zipCode = "11111",
                    detailAddress = "1동",
                ),
                payMethod = PayMethod.CARD,
                orderItems = listOf(
                    PlaceOrderInput.OrderLineInput(
                        productId = product.id,
                        quantity = 1,
                        unitPrice = 15000,
                    ),
                ),
            )

            val input2 = PlaceOrderInput(
                loginId = LoginId("user2"),
                address = AddressInput(
                    street = "테헤란로 2",
                    city = "서울시",
                    zipCode = "22222",
                    detailAddress = "2동",
                ),
                payMethod = PayMethod.POINT,
                orderItems = listOf(
                    PlaceOrderInput.OrderLineInput(
                        productId = product.id,
                        quantity = 3,
                        unitPrice = 15000,
                    ),
                ),
            )

            val result1 = orderFacade.placeOrder(input1)
            val result2 = orderFacade.placeOrder(input2)

            Then("각각의 주문이 독립적으로 정상 생성된다") {
                result1.id shouldNotBe result2.id
                result1.totalAmount shouldBe 15000L
                result2.totalAmount shouldBe 45000L

                val order1 = orderJpaRepository.findByIdWithOrderLines(result1.id)!!
                val order2 = orderJpaRepository.findByIdWithOrderLines(result2.id)!!

                order1.userId shouldBe user1.id
                order2.userId shouldBe user2.id
                order1.deliveryAddress.detailAddress shouldBe "1동"
                order2.deliveryAddress.detailAddress shouldBe "2동"
            }
        }
    }
})
