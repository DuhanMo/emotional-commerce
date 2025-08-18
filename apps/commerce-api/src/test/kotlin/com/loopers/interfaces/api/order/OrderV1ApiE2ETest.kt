package com.loopers.interfaces.api.order

import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.point.Point
import com.loopers.domain.product.ProductSummary
import com.loopers.domain.user.LoginId
import com.loopers.infrastructure.order.OrderJpaRepository
import com.loopers.infrastructure.point.PointJpaRepository
import com.loopers.infrastructure.product.ProductJpaRepository
import com.loopers.infrastructure.product.ProductSummaryJpaRepository
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.interfaces.api.ApiResponse
import com.loopers.interfaces.api.order.PlaceOrderRequest.AddressRequest
import com.loopers.interfaces.api.order.PlaceOrderRequest.OrderLineRequest
import com.loopers.support.fixture.createProduct
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.E2ESpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class OrderV1ApiE2ETest(
    private val userJpaRepository: UserJpaRepository,
    private val productJpaRepository: ProductJpaRepository,
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
    private val orderJpaRepository: OrderJpaRepository,
    private val testRestTemplate: TestRestTemplate,
    private val pointJpaRepository: PointJpaRepository,
) : E2ESpec({
    /**
     * @see OrderV1Controller.placeOrder
     */
    describe("POST /api/v1/orders") {
        val url = "/api/v1/orders"

        it("주문 생성이 성공하는 경우 - 생성된 주문 정보를 응답으로 반환한다") {
            // Given
            val user = userJpaRepository.save(createUser(LoginId("testuser1")))
            val product1 = productJpaRepository.save(createProduct(name = "상품1", price = 10000))
            val product2 = productJpaRepository.save(createProduct(name = "상품2", price = 5000))
            productSummaryJpaRepository.save(ProductSummary(productId = product1.id, likeCount = 0L))
            productSummaryJpaRepository.save(ProductSummary(productId = product2.id, likeCount = 0L))
            pointJpaRepository.save(Point(userId = user.id, amount = 100_000L))

            val request = PlaceOrderRequest(
                address = AddressRequest(
                    street = "강남대로 123",
                    city = "서울시",
                    zipCode = "12345",
                    detailAddress = "101동 1001호",
                ),
                paymentMethod = PaymentMethod.POINT,
                orderItems = listOf(
                    OrderLineRequest(
                        productId = product1.id,
                        quantity = 2,
                        unitPrice = 10000,
                    ),
                    OrderLineRequest(
                        productId = product2.id,
                        quantity = 1,
                        unitPrice = 5000,
                    ),
                ),
                issuedCouponId = null,
            )
            val headers = HttpHeaders().apply { set("X-USER-ID", "testuser1") }
            val responseType = object : ParameterizedTypeReference<ApiResponse<PlaceOrderResponse>>() {}

            // When
            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request, headers), responseType)

            // Then
            response.statusCode.is2xxSuccessful shouldBe true
            response.body?.data?.id shouldNotBe null
            response.body?.data?.userId shouldBe user.id
            response.body?.data?.totalAmount shouldBe 25000L
            response.body?.data?.orderLines?.size shouldBe 2

            // 데이터베이스 저장 확인
            val savedOrder = orderJpaRepository.findByIdWithOrderLines(response.body?.data?.id!!)
            savedOrder shouldNotBe null
            savedOrder!!.userId shouldBe user.id
            savedOrder.totalAmount shouldBe 25_000L
            savedOrder.orderLines.size shouldBe 2

        }

        it("X-USER-ID 헤더가 없는 경우 - 400 Bad Request 응답을 반환한다") {
            // Given
            val request = PlaceOrderRequest(
                address = AddressRequest(
                    street = "강남대로 123",
                    city = "서울시",
                    zipCode = "12345",
                    detailAddress = null,
                ),
                paymentMethod = PaymentMethod.POINT,
                orderItems = listOf(
                    OrderLineRequest(
                        productId = 1L,
                        quantity = 1,
                        unitPrice = 10000,
                    ),
                ),
                issuedCouponId = null,
            )
            val responseType = object : ParameterizedTypeReference<ApiResponse<PlaceOrderResponse>>() {}

            // When
            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request), responseType)

            // Then
            response.statusCode shouldBe HttpStatus.BAD_REQUEST
        }

        it("존재하지 않는 사용자인 경우 - 404 Not Found 응답을 반환한다") {
            // Given
            val request = PlaceOrderRequest(
                address = AddressRequest(
                    street = "강남대로 123",
                    city = "서울시",
                    zipCode = "12345",
                    detailAddress = null,
                ),
                paymentMethod = PaymentMethod.POINT,
                orderItems = listOf(
                    OrderLineRequest(
                        productId = 1L,
                        quantity = 1,
                        unitPrice = 10000,
                    ),
                ),
                issuedCouponId = null,
            )
            val headers = HttpHeaders().apply { set("X-USER-ID", "notexist1") }
            val responseType = object : ParameterizedTypeReference<ApiResponse<PlaceOrderResponse>>() {}

            // When
            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request, headers), responseType)

            // Then
            response.statusCode shouldBe HttpStatus.NOT_FOUND
        }

        it("주문 상품이 빈 목록인 경우 - 400 Bad Request 응답을 반환한다") {
            // Given
            val user = userJpaRepository.save(createUser(LoginId("testuser2")))

            val request = PlaceOrderRequest(
                address = AddressRequest(
                    street = "강남대로 123",
                    city = "서울시",
                    zipCode = "12345",
                    detailAddress = null,
                ),
                paymentMethod = PaymentMethod.POINT,
                orderItems = emptyList(),
                issuedCouponId = null,
            )
            val headers = HttpHeaders().apply { set("X-USER-ID", "testuser2") }
            val responseType = object : ParameterizedTypeReference<ApiResponse<PlaceOrderResponse>>() {}

            // When
            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request, headers), responseType)

            // Then
            response.statusCode shouldBe HttpStatus.BAD_REQUEST
        }

        it("결제 방법이 null인 경우 - 400 Bad Request 응답을 반환한다") {
            // Given
            val user = userJpaRepository.save(createUser(LoginId("testuser3")))

            val request = PlaceOrderRequest(
                address = AddressRequest(
                    street = "강남대로 123",
                    city = "서울시",
                    zipCode = "12345",
                    detailAddress = null,
                ),
                paymentMethod = null,
                orderItems = listOf(
                    OrderLineRequest(
                        productId = 1L,
                        quantity = 1,
                        unitPrice = 10000,
                    ),
                ),
                issuedCouponId = null,
            )
            val headers = HttpHeaders().apply { set("X-USER-ID", "testuser3") }
            val responseType = object : ParameterizedTypeReference<ApiResponse<PlaceOrderResponse>>() {}

            // When
            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request, headers), responseType)

            // Then
            response.statusCode shouldBe HttpStatus.BAD_REQUEST
        }

        it("주문 상품의 수량이 음수인 경우 - 400 Bad Request 응답을 반환한다") {
            // Given
            val user = userJpaRepository.save(createUser(LoginId("testuser4")))

            val request = PlaceOrderRequest(
                address = AddressRequest(
                    street = "강남대로 123",
                    city = "서울시",
                    zipCode = "12345",
                    detailAddress = null,
                ),
                paymentMethod = PaymentMethod.POINT,
                orderItems = listOf(
                    OrderLineRequest(
                        productId = 1L,
                        quantity = -1,
                        unitPrice = 10000,
                    ),
                ),
                issuedCouponId = null,
            )
            val headers = HttpHeaders().apply { set("X-USER-ID", "testuser4") }
            val responseType = object : ParameterizedTypeReference<ApiResponse<PlaceOrderResponse>>() {}

            // When
            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request, headers), responseType)

            // Then
            response.statusCode shouldBe HttpStatus.BAD_REQUEST
        }
    }
})
