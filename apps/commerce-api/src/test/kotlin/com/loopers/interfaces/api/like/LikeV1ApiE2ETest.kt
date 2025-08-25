package com.loopers.interfaces.api.like

import com.loopers.infrastructure.brand.BrandJpaRepository
import com.loopers.infrastructure.product.ProductJpaRepository
import com.loopers.infrastructure.product.ProductLikeJpaRepository
import com.loopers.infrastructure.product.ProductSummaryJpaRepository
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.support.tests.E2ESpec
import org.springframework.boot.test.web.client.TestRestTemplate

class LikeV1ApiE2ETest(
    private val userJpaRepository: UserJpaRepository,
    private val productJpaRepository: ProductJpaRepository,
    private val productLikeJpaRepository: ProductLikeJpaRepository,
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
    private val brandJpaRepository: BrandJpaRepository,
    private val testRestTemplate: TestRestTemplate,
) : E2ESpec({
//    /**
//     * @see LikeV1Controller.likeProduct
//     */
//    describe("POST /api/v1/like/products/{productId}") {
//        it("존재하는 사용자와 상품으로 좋아요 등록하는 경우 - 성공 응답을 반환한다") {
//            // Given
//            val user = userJpaRepository.save(createUser(loginId = LoginId("user123")))
//            val product = productJpaRepository.save(createProduct())
//            productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 5L))
//
//            val url = "/api/v1/like/products/${product.id}"
//            val headers = HttpHeaders().apply { set("X-USER-ID", "user123") }
//            val responseType = object : ParameterizedTypeReference<ApiResponse<Any>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.meta?.result shouldBe SUCCESS
//
//            // 좋아요 데이터 확인
//            val productLike = productLikeJpaRepository.findAll()
//                .first { it.productId == product.id && it.userId == user.id }
//            productLike.status shouldBe ProductLikeStatus.ACTIVE
//
//            // 집계 데이터 확인
//            val productSummary = productSummaryJpaRepository.findByProductId(product.id)!!
//            productSummary.likeCount shouldBe 6L
//        }
//
//        it("이미 좋아요한 상품을 다시 좋아요하는 경우 - 성공 응답을 반환한다") {
//            // Given
//            val user = userJpaRepository.save(createUser(loginId = LoginId("user456")))
//            val product = productJpaRepository.save(createProduct())
//            productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 10L))
//            productLikeJpaRepository.save(
//                ProductLike(
//                    productId = product.id,
//                    userId = user.id,
//                    status = ProductLikeStatus.ACTIVE,
//                ),
//            )
//
//            val url = "/api/v1/like/products/${product.id}"
//            val headers = HttpHeaders().apply { set("X-USER-ID", "user456") }
//            val responseType = object : ParameterizedTypeReference<ApiResponse<Any>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.meta?.result shouldBe SUCCESS
//
//            // 좋아요 상태는 여전히 ACTIVE
//            val productLike = productLikeJpaRepository.findAll()
//                .first { it.productId == product.id && it.userId == user.id }
//            productLike.status shouldBe ProductLikeStatus.ACTIVE
//
//            // 집계 데이터는 증가하지 않음
//            val productSummary = productSummaryJpaRepository.findByProductId(product.id)!!
//            productSummary.likeCount shouldBe 10L
//        }
//
//        it("취소했던 상품을 다시 좋아요하는 경우 - 성공 응답을 반환한다") {
//            // Given
//            val user = userJpaRepository.save(createUser(loginId = LoginId("user789")))
//            val product = productJpaRepository.save(createProduct())
//            productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 3L))
//            productLikeJpaRepository.save(
//                ProductLike(
//                    productId = product.id,
//                    userId = user.id,
//                    status = ProductLikeStatus.DELETED,
//                ),
//            )
//
//            val url = "/api/v1/like/products/${product.id}"
//            val headers = HttpHeaders().apply { set("X-USER-ID", "user789") }
//            val responseType = object : ParameterizedTypeReference<ApiResponse<Any>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.meta?.result shouldBe SUCCESS
//
//            // 좋아요 상태가 ACTIVE로 변경
//            val productLike = productLikeJpaRepository.findAll()
//                .first { it.productId == product.id && it.userId == user.id }
//            productLike.status shouldBe ProductLikeStatus.ACTIVE
//
//            // 집계 데이터 증가
//            val productSummary = productSummaryJpaRepository.findByProductId(product.id)!!
//            productSummary.likeCount shouldBe 4L
//        }
//
//        it("X-USER-ID 헤더가 없는 경우 - 400 Bad Request 응답을 반환한다") {
//            // Given
//            val product = productJpaRepository.save(createProduct())
//            val url = "/api/v1/like/products/${product.id}"
//            val headers = HttpHeaders()
//            val responseType = object : ParameterizedTypeReference<ApiResponse<Any>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode shouldBe HttpStatus.BAD_REQUEST
//        }
//
//        it("존재하지 않는 사용자로 요청하는 경우 - 404 Not Found 응답을 반환한다") {
//            // Given
//            val product = productJpaRepository.save(createProduct())
//            productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 0L))
//
//            val url = "/api/v1/like/products/${product.id}"
//            val headers = HttpHeaders().apply { set("X-USER-ID", "zxy999") }
//            val responseType = object : ParameterizedTypeReference<ApiResponse<Any>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode shouldBe HttpStatus.NOT_FOUND
//        }
//    }
//
//    /**
//     * @see LikeV1Controller.unlikeProduct
//     */
//    describe("DELETE /api/v1/like/products/{productId}") {
//        it("좋아요한 상품을 취소하는 경우 - 성공 응답을 반환한다") {
//            // Given
//            val user = userJpaRepository.save(createUser(loginId = LoginId("user111")))
//            val product = productJpaRepository.save(createProduct(likeCount = 1))
//            productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 8L))
//            productLikeJpaRepository.save(ProductLike(product.id, user.id, ProductLikeStatus.ACTIVE))
//
//            val url = "/api/v1/like/products/${product.id}"
//            val headers = HttpHeaders().apply { set("X-USER-ID", "user111") }
//            val responseType = object : ParameterizedTypeReference<ApiResponse<Any>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.meta?.result shouldBe SUCCESS
//
//            // 좋아요 상태가 DELETED로 변경
//            val productLike = productLikeJpaRepository.findAll()
//                .first { it.productId == product.id && it.userId == user.id }
//            productLike.status shouldBe ProductLikeStatus.DELETED
//
//            // 집계 데이터 감소
//            val productSummary = productSummaryJpaRepository.findByProductId(product.id)!!
//            productSummary.likeCount shouldBe 7L
//        }
//
//        it("이미 취소한 상품을 다시 취소하는 경우 - 성공 응답을 반환한다") {
//            // Given
//            val user = userJpaRepository.save(createUser(loginId = LoginId("user222")))
//            val product = productJpaRepository.save(createProduct())
//            productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 5L))
//            productLikeJpaRepository.save(
//                ProductLike(
//                    productId = product.id,
//                    userId = user.id,
//                    status = ProductLikeStatus.DELETED,
//                ),
//            )
//
//            val url = "/api/v1/like/products/${product.id}"
//            val headers = HttpHeaders().apply { set("X-USER-ID", "user222") }
//            val responseType = object : ParameterizedTypeReference<ApiResponse<Any>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.meta?.result shouldBe SUCCESS
//
//            // 좋아요 상태는 여전히 DELETED
//            val productLike = productLikeJpaRepository.findAll()
//                .first { it.productId == product.id && it.userId == user.id }
//            productLike.status shouldBe ProductLikeStatus.DELETED
//
//            // 집계 데이터는 감소하지 않음
//            val productSummary = productSummaryJpaRepository.findByProductId(product.id)!!
//            productSummary.likeCount shouldBe 5L
//        }
//
//        it("X-USER-ID 헤더가 없는 경우 - 400 Bad Request 응답을 반환한다") {
//            // Given
//            val product = productJpaRepository.save(createProduct())
//            val url = "/api/v1/like/products/${product.id}"
//            val headers = HttpHeaders()
//            val responseType = object : ParameterizedTypeReference<ApiResponse<Any>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode shouldBe HttpStatus.BAD_REQUEST
//        }
//
//        it("존재하지 않는 사용자로 요청하는 경우 - 404 Not Found 응답을 반환한다") {
//            // Given
//            val product = productJpaRepository.save(createProduct())
//            productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 1L))
//
//            val url = "/api/v1/like/products/${product.id}"
//            val headers = HttpHeaders().apply { set("X-USER-ID", "zxy999") }
//            val responseType = object : ParameterizedTypeReference<ApiResponse<Any>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode shouldBe HttpStatus.NOT_FOUND
//        }
//    }
//
//    /**
//     * @see LikeV1Controller.getLikedProducts
//     */
//    describe("GET /api/v1/like/products") {
//        val url = "/api/v1/like/products"
//
//        it("좋아요한 상품이 있는 경우 - 좋아요한 상품 목록을 반환한다") {
//            // Given
//            val user = userJpaRepository.save(createUser(loginId = LoginId("user123")))
//            val brand = brandJpaRepository.save(createBrand())
//            val product1 = productJpaRepository.save(createProduct(brandId = brand.id, name = "좋아요 상품1"))
//            val product2 = productJpaRepository.save(createProduct(brandId = brand.id, name = "좋아요 상품2"))
//            val product3 = productJpaRepository.save(createProduct(brandId = brand.id, name = "일반 상품"))
//
//            productSummaryJpaRepository.save(ProductSummary(productId = product1.id, likeCount = 15L))
//            productSummaryJpaRepository.save(ProductSummary(productId = product2.id, likeCount = 25L))
//            productSummaryJpaRepository.save(ProductSummary(productId = product3.id, likeCount = 5L))
//
//            // user가 product1, product2만 좋아요
//            productLikeJpaRepository.save(ProductLike(product1.id, user.id, ProductLikeStatus.ACTIVE))
//            productLikeJpaRepository.save(ProductLike(product2.id, user.id, ProductLikeStatus.ACTIVE))
//
//            val headers = HttpHeaders().apply { set("X-USER-ID", "user123") }
//            val responseType = object : ParameterizedTypeReference<ApiResponse<List<ProductItemResponse>>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            val products = response.body?.data!!
//            products shouldHaveSize 2
//
//            // 좋아요한 상품들만 포함되는지 확인
//            val productNames = products.map { it.name }
//            productNames.contains("좋아요 상품1") shouldBe true
//            productNames.contains("좋아요 상품2") shouldBe true
//            productNames.contains("일반 상품") shouldBe false
//
//            // 상품 정보가 올바르게 포함되는지 확인
//            val firstProduct = products.first { it.name == "좋아요 상품1" }
//            firstProduct.summary.likeCount shouldBe 15L
//        }
//
//        it("좋아요한 상품이 없는 경우 - 빈 목록을 반환한다") {
//            // Given
//            userJpaRepository.save(createUser(loginId = LoginId("user456")))
//            val brand = brandJpaRepository.save(createBrand())
//            val product = productJpaRepository.save(createProduct(brandId = brand.id))
//            productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 10L))
//
//            val headers = HttpHeaders().apply { set("X-USER-ID", "user456") }
//            val responseType = object : ParameterizedTypeReference<ApiResponse<List<ProductItemResponse>>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.data!! shouldHaveSize 0
//        }
//
//        it("좋아요를 취소한 상품만 있는 경우 - 빈 목록을 반환한다") {
//            // Given
//            val user = userJpaRepository.save(createUser(loginId = LoginId("user789")))
//            val product1 = productJpaRepository.save(createProduct(name = "취소한 상품1"))
//            val product2 = productJpaRepository.save(createProduct(name = "취소한 상품2"))
//
//            productSummaryJpaRepository.save(ProductSummary(productId = product1.id, likeCount = 5L))
//            productSummaryJpaRepository.save(ProductSummary(productId = product2.id, likeCount = 3L))
//
//            // 모든 상품을 좋아요 취소 상태로 설정
//            productLikeJpaRepository.save(ProductLike(product1.id, user.id, ProductLikeStatus.DELETED))
//            productLikeJpaRepository.save(ProductLike(product2.id, user.id, ProductLikeStatus.DELETED))
//
//            val headers = HttpHeaders().apply { set("X-USER-ID", "user789") }
//            val responseType = object : ParameterizedTypeReference<ApiResponse<List<ProductItemResponse>>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.data!! shouldHaveSize 0
//        }
//
//        it("X-USER-ID 헤더가 없는 경우 - 400 Bad Request 응답을 반환한다") {
//            // Given
//            val headers = HttpHeaders()
//            val responseType = object : ParameterizedTypeReference<ApiResponse<List<ProductItemResponse>>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode shouldBe HttpStatus.BAD_REQUEST
//        }
//
//        it("존재하지 않는 사용자로 요청하는 경우 - 404 Not Found 응답을 반환한다") {
//            // Given
//            val headers = HttpHeaders().apply { set("X-USER-ID", "empty123") }
//            val responseType = object : ParameterizedTypeReference<ApiResponse<List<ProductItemResponse>>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode shouldBe HttpStatus.NOT_FOUND
//        }
//
//        it("여러 사용자가 좋아요한 상품들 중 특정 사용자만의 좋아요 목록을 반환한다") {
//            // Given
//            val user1 = userJpaRepository.save(createUser(loginId = LoginId("user111")))
//            val user2 = userJpaRepository.save(createUser(loginId = LoginId("user222")))
//            val brand = brandJpaRepository.save(createBrand())
//            val product1 = productJpaRepository.save(createProduct(brandId = brand.id, name = "공통 좋아요 상품"))
//            val product2 = productJpaRepository.save(createProduct(brandId = brand.id, name = "user1만 좋아요"))
//            val product3 = productJpaRepository.save(createProduct(brandId = brand.id, name = "user2만 좋아요"))
//
//            productSummaryJpaRepository.save(ProductSummary(productId = product1.id, likeCount = 10L))
//            productSummaryJpaRepository.save(ProductSummary(productId = product2.id, likeCount = 5L))
//            productSummaryJpaRepository.save(ProductSummary(productId = product3.id, likeCount = 7L))
//
//            // user1은 product1, product2 좋아요
//            productLikeJpaRepository.save(ProductLike(product1.id, user1.id, ProductLikeStatus.ACTIVE))
//            productLikeJpaRepository.save(ProductLike(product2.id, user1.id, ProductLikeStatus.ACTIVE))
//
//            // user2는 product1, product3 좋아요
//            productLikeJpaRepository.save(ProductLike(product1.id, user2.id, ProductLikeStatus.ACTIVE))
//            productLikeJpaRepository.save(ProductLike(product3.id, user2.id, ProductLikeStatus.ACTIVE))
//
//            val headers = HttpHeaders().apply { set("X-USER-ID", "user111") }
//            val responseType = object : ParameterizedTypeReference<ApiResponse<List<ProductItemResponse>>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            val products = response.body?.data!!
//            products shouldHaveSize 2
//
//            val productNames = products.map { it.name }
//            productNames.contains("공통 좋아요 상품") shouldBe true
//            productNames.contains("user1만 좋아요") shouldBe true
//            productNames.contains("user2만 좋아요") shouldBe false
//        }
//    }
})
