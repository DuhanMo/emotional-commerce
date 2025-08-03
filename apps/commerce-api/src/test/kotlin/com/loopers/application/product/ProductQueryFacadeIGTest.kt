package com.loopers.application.product

import com.loopers.domain.support.PageCriteria
import com.loopers.infrastructure.brand.BrandJpaRepository
import com.loopers.infrastructure.product.ProductJpaRepository
import com.loopers.infrastructure.product.ProductSummaryJpaRepository
import com.loopers.support.fixture.createBrand
import com.loopers.support.fixture.createProduct
import com.loopers.support.fixture.createProductSummary
import com.loopers.support.tests.IntegrationSpec
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ProductQueryFacadeIGTest(
    private val productQueryFacade: ProductQueryFacade,
    private val brandJpaRepository: BrandJpaRepository,
    private val productJpaRepository: ProductJpaRepository,
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
) : IntegrationSpec({

    Given("브랜드 정보와 상품 정보가 모두 존재하는 경우") {
        val brand1 = brandJpaRepository.save(createBrand(name = "브랜드1", description = "브랜드1 설명"))
        val brand2 = brandJpaRepository.save(createBrand(name = "브랜드2", description = "브랜드2 설명"))

        val product1 = productJpaRepository.save(createProduct(brandId = brand1.id, name = "상품1"))
        val product2 = productJpaRepository.save(createProduct(brandId = brand2.id, name = "상품2"))

        productSummaryJpaRepository.save(createProductSummary(product1.id, likeCount = 100))
        productSummaryJpaRepository.save(createProductSummary(product2.id, likeCount = 200))

        When("상품 목록을 조회하면") {
            val result = productQueryFacade.findProducts(
                brandId = null,
                sortBy = "latest",
                pageCriteria = PageCriteria(0, 10),
            )

            Then("상품 정보와 브랜드 정보가 모두 포함된 응답을 반환한다") {
                result.products shouldHaveSize 2
                result.totalCount shouldBe 2L
                result.hasMore shouldBe false

                val products = result.products

                // 상품 정보와 브랜드 정보가 잘 담겨있는지 검증.
                products.forEach { productItemOutput ->
                    productItemOutput.brand shouldNotBe null
                    productItemOutput.summary shouldNotBe null
                }

                // 브랜드 정보가 올바르게 매핑되었는지 검증.
                val product1Output = products.first { it.id == product1.id }
                product1Output.name shouldBe "상품1"
                product1Output.brand.name shouldBe "브랜드1"
                product1Output.brand.description shouldBe "브랜드1 설명"
                product1Output.summary.likeCount shouldBe 100

                val product2Output = products.first { it.id == product2.id }
                product2Output.name shouldBe "상품2"
                product2Output.brand.name shouldBe "브랜드2"
                product2Output.brand.description shouldBe "브랜드2 설명"
                product2Output.summary.likeCount shouldBe 200
            }
        }
    }

    Given("브랜드 정보가 존재하지 않는 경우") {
        val product = productJpaRepository.save(createProduct(brandId = 999L, name = "브랜드 미존재 상품"))
        productSummaryJpaRepository.save(createProductSummary(product.id, likeCount = 50))

        When("상품 목록을 조회하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    productQueryFacade.findProducts(
                        brandId = null,
                        sortBy = "latest",
                        pageCriteria = PageCriteria(0, 10),
                    )
                }
            }
        }
    }
})
