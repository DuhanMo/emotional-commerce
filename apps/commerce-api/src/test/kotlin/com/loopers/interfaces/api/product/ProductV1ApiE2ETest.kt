package com.loopers.interfaces.api.product

import com.loopers.infrastructure.brand.BrandJpaRepository
import com.loopers.infrastructure.product.ProductJpaRepository
import com.loopers.infrastructure.product.ProductSummaryJpaRepository
import com.loopers.interfaces.api.ApiResponse
import com.loopers.support.fixture.createBrand
import com.loopers.support.fixture.createProduct
import com.loopers.support.fixture.createProductSummary
import com.loopers.support.tests.E2ESpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class ProductV1ApiE2ETest(
    private val brandJpaRepository: BrandJpaRepository,
    private val productJpaRepository: ProductJpaRepository,
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
    private val testRestTemplate: TestRestTemplate,
) : E2ESpec({
    /**
     * @see ProductV1Controller.getProducts
     */
    describe("GET /api/v1/products") {
        val url = "/api/v1/products"

        context("상품 목록이 존재하는 경우") {
            val brand1 = brandJpaRepository.save(createBrand(name = "브랜드1", description = "브랜드1 설명"))
            val brand2 = brandJpaRepository.save(createBrand(name = "브랜드2", description = "브랜드2 설명"))
            
            val product1 = productJpaRepository.save(
                createProduct(
                    brandId = brand1.id,
                    name = "상품1",
                    description = "상품1 설명",
                    price = 10_000,
                    stock = 50
                )
            )
            val product2 = productJpaRepository.save(
                createProduct(
                    brandId = brand2.id,
                    name = "상품2", 
                    description = "상품2 설명",
                    price = 20_000,
                    stock = 30
                )
            )
            
            productSummaryJpaRepository.save(createProductSummary(product1.id, likeCount = 100))
            productSummaryJpaRepository.save(createProductSummary(product2.id, likeCount = 200))

            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductListResponse>>() {}

            it("상품 목록을 조회하면, 상품 정보와 브랜드 정보가 포함된 응답을 반환한다") {
                val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType)

                response.statusCode.is2xxSuccessful shouldBe true
                response.body?.data?.products!! shouldHaveSize 2
                response.body?.data?.totalCount shouldBe 2L
                response.body?.data?.hasMore shouldBe false

                val firstProduct = response.body?.data?.products?.first()
                firstProduct?.name shouldBe "상품2"
                firstProduct?.price shouldBe 20_000
                firstProduct?.brand?.name shouldBe "브랜드2"
                firstProduct?.summary?.likeCount shouldBe 200
            }
        }

        context("페이징 파라미터를 사용하는 경우") {
            val brand = brandJpaRepository.save(createBrand(name = "테스트 브랜드"))
            
            // 5개의 상품 생성
            repeat(5) { index ->
                val product = productJpaRepository.save(
                    createProduct(
                        brandId = brand.id,
                        name = "상품${index + 1}",
                        price = (index + 1) * 1000
                    )
                )
                productSummaryJpaRepository.save(createProductSummary(product.id, likeCount = (index + 1) * 10))
            }

            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductListResponse>>() {}

            it("페이지 크기를 2로 설정하면, 2개의 상품만 조회된다") {
                val response = testRestTemplate.exchange(
                    "$url?page=0&size=2", 
                    HttpMethod.GET, 
                    HttpEntity.EMPTY, 
                    responseType
                )

                response.statusCode.is2xxSuccessful shouldBe true
                response.body?.data?.products!! shouldHaveSize 2
                response.body?.data?.totalCount shouldBe 5L
                response.body?.data?.hasMore shouldBe true
            }
        }

        context("정렬 파라미터를 사용하는 경우") {
            val brand = brandJpaRepository.save(createBrand(name = "테스트 브랜드"))
            
            val product1 = productJpaRepository.save(
                createProduct(brandId = brand.id, name = "저렴한 상품", price = 5_000)
            )
            val product2 = productJpaRepository.save(
                createProduct(brandId = brand.id, name = "비싼 상품", price = 50_000)
            )
            
            productSummaryJpaRepository.save(createProductSummary(product1.id, likeCount = 10))
            productSummaryJpaRepository.save(createProductSummary(product2.id, likeCount = 100))

            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductListResponse>>() {}

            it("가격 오름차순으로 정렬하면, 저렴한 상품이 먼저 조회된다") {
                val response = testRestTemplate.exchange(
                    "$url?sortBy=price_asc", 
                    HttpMethod.GET, 
                    HttpEntity.EMPTY, 
                    responseType
                )

                response.statusCode.is2xxSuccessful shouldBe true
                response.body?.data?.products!! shouldHaveSize 2
                response.body?.data?.products?.first()?.name shouldBe "저렴한 상품"
                response.body?.data?.products?.first()?.price shouldBe 5000
            }

            it("좋아요 내림차순으로 정렬하면, 좋아요가 많은 상품이 먼저 조회된다") {
                val response = testRestTemplate.exchange(
                    "$url?sortBy=likes_desc", 
                    HttpMethod.GET, 
                    HttpEntity.EMPTY, 
                    responseType
                )

                response.statusCode.is2xxSuccessful shouldBe true
                response.body?.data?.products!! shouldHaveSize 2
                response.body?.data?.products?.first()?.name shouldBe "비싼 상품"
                response.body?.data?.products?.first()?.summary?.likeCount shouldBe 100
            }
        }

        context("상품이 존재하지 않는 경우") {
            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductListResponse>>() {}

            it("빈 목록을 반환한다") {
                val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType)

                response.statusCode.is2xxSuccessful shouldBe true
                response.body?.data?.products!! shouldHaveSize 0
                response.body?.data?.totalCount shouldBe 0L
                response.body?.data?.hasMore shouldBe false
            }
        }

        context("잘못된 정렬 파라미터를 사용하는 경우") {
            val brand = brandJpaRepository.save(createBrand(name = "테스트 브랜드"))
            val product = productJpaRepository.save(createProduct(brandId = brand.id))
            productSummaryJpaRepository.save(createProductSummary(product.id))

            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductListResponse>>() {}

            it("기본 정렬(latest)로 조회된다") {
                val response = testRestTemplate.exchange(
                    "$url?sortBy=invalid_sort", 
                    HttpMethod.GET, 
                    HttpEntity.EMPTY, 
                    responseType
                )

                response.statusCode.is2xxSuccessful shouldBe true
                response.body?.data?.products!! shouldHaveSize 1
            }
        }
    }
})
