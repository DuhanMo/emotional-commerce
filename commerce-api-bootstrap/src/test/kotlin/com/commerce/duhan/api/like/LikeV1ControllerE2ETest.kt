package com.commerce.duhan.api.like

import com.commerce.duhan.api.support.ApiResponse
import com.commerce.duhan.api.support.ApiResponse.Metadata.Result
import com.commerce.duhan.db.product.ProductJpaRepository
import com.commerce.duhan.db.product.ProductLikeJpaRepository
import com.commerce.duhan.domain.fixtures.createProduct
import com.commerce.duhan.domain.fixtures.createProductLike
import com.commerce.duhan.domain.product.ProductLikeStatus
import com.commerce.duhan.support.E2ESpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class LikeV1ControllerE2ETest(
    private val testRestTemplate: TestRestTemplate,
    private val productJpaRepository: ProductJpaRepository,
    private val productLikeJpaRepository: ProductLikeJpaRepository,
) : E2ESpec({
    /**
     * @see LikeV1Controller.addProductLike
     */
    describe("POST /api/v1/like/products/{productId}") {
        context("상품 좋아요를 추가하는 경우") {
            it("성공 응답을 반환하고 상품 좋아요가 저장된다") {
                // given
                val product = productJpaRepository.save(createProduct())
                val userId = 1L
                val headers = HttpHeaders().apply { set("X-USER-ID", "$userId") }
                val responseType = object : ParameterizedTypeReference<ApiResponse<Any>>() {}
                val url = "/api/v1/like/products/${product.id}"

                // when
                val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(null, headers), responseType)

                // then
                response.statusCode shouldBe HttpStatus.OK
                response.body?.meta?.result shouldBe Result.SUCCESS

                val savedProduct = productJpaRepository.findById(product.id).orElseThrow()
                savedProduct.likeCount shouldBe 1

                val savedLike = productLikeJpaRepository.findByUserIdAndProductId(userId, product.id)
                savedLike?.status shouldBe ProductLikeStatus.ACTIVE
            }
        }

        context("존재하지 않는 상품에 좋아요를 추가하는 경우") {
            it("404 Not Found 응답을 반환한다") {
                // given
                val headers = HttpHeaders().apply { set("X-USER-ID", "1") }
                val responseType = object : ParameterizedTypeReference<ApiResponse<Any>>() {}
                val url = "/api/v1/like/products/9999"

                // when
                val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(null, headers), responseType)

                // then
                response.statusCode shouldBe HttpStatus.NOT_FOUND
                response.body?.meta?.result shouldBe Result.FAIL
            }
        }
    }

    /**
     * @see LikeV1Controller.removeProductLike
     */
    describe("DELETE /api/v1/like/products/{productId}") {
        context("상품 좋아요를 삭제하는 경우") {
            it("성공 응답을 반환하고 상품 좋아요가 삭제 상태로 변경된다") {
                // given
                val product = productJpaRepository.save(createProduct(likeCount = 1))
                val userId = 1L
                productLikeJpaRepository.save(createProductLike(userId = userId, productId = product.id, status = ProductLikeStatus.ACTIVE))

                val headers = HttpHeaders().apply { set("X-USER-ID", "$userId") }
                val responseType = object : ParameterizedTypeReference<ApiResponse<Any>>() {}
                val url = "/api/v1/like/products/${product.id}"

                // when
                val response = testRestTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, headers), responseType)

                // then
                response.statusCode shouldBe HttpStatus.OK
                response.body?.meta?.result shouldBe Result.SUCCESS

                val updatedProduct = productJpaRepository.findById(product.id).orElseThrow()
                updatedProduct.likeCount shouldBe 0

                val updatedLike = productLikeJpaRepository.findByUserIdAndProductId(userId, product.id)
                updatedLike?.status shouldBe ProductLikeStatus.DELETED
            }
        }

        context("존재하지 않는 상품의 좋아요를 삭제하는 경우") {
            it("404 Not Found 응답을 반환한다") {
                // given
                val headers = HttpHeaders().apply { set("X-USER-ID", "1") }
                val responseType = object : ParameterizedTypeReference<ApiResponse<Any>>() {}
                val url = "/api/v1/like/products/9999"

                // when
                val response = testRestTemplate.exchange(url, HttpMethod.DELETE, HttpEntity(null, headers), responseType)

                // then
                response.statusCode shouldBe HttpStatus.NOT_FOUND
                response.body?.meta?.result shouldBe Result.FAIL
            }
        }
    }
})
