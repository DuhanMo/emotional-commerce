package com.loopers.domain.product

import com.loopers.domain.support.PageCriteria
import com.loopers.infrastructure.product.ProductJpaRepository
import com.loopers.infrastructure.product.ProductSummaryJpaRepository
import com.loopers.support.fixture.createProduct
import com.loopers.support.tests.IntegrationSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class ProductQueryServiceIGTest(
    private val productQueryService: ProductQueryService,
    private val productJpaRepository: ProductJpaRepository,
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
) : IntegrationSpec({
    Given("좋아요 많은 순으로 정렬하는 경우") {
        val sortBy = "likes_desc"

        val product1 = productJpaRepository.save(createProduct())
        val product2 = productJpaRepository.save(createProduct())
        val product3 = productJpaRepository.save(createProduct())
        val product4 = productJpaRepository.save(createProduct())
        val product5 = productJpaRepository.save(createProduct())
        productSummaryJpaRepository.save(ProductSummary(product1.id, 1))
        productSummaryJpaRepository.save(ProductSummary(product2.id, 2))
        productSummaryJpaRepository.save(ProductSummary(product3.id, 20))
        productSummaryJpaRepository.save(ProductSummary(product4.id, 4))
        productSummaryJpaRepository.save(ProductSummary(product5.id, 5))

        When("상품 목록을 조회하면") {
            val result = productQueryService.findProducts(
                brandId = null,
                sortBy = sortBy,
                pageCriteria = PageCriteria(0, 20)
            ).content

            Then("좋아요 순으로 정렬된다") {
                result shouldHaveSize 5
                result[0].product.id shouldBe product3.id
                result[1].product.id shouldBe product5.id
                result[2].product.id shouldBe product4.id
                result[3].product.id shouldBe product2.id
                result[4].product.id shouldBe product1.id
            }
        }
    }

    Given("가격 낮은 순으로 정렬하는 경우") {
        val sortBy = "price_asc"

        val product1 = productJpaRepository.save(createProduct(price = 1_000))
        val product2 = productJpaRepository.save(createProduct(price = 5_000))
        val product3 = productJpaRepository.save(createProduct(price = 2_000))
        val product4 = productJpaRepository.save(createProduct(price = 500))
        val product5 = productJpaRepository.save(createProduct(price = 100))

        When("상품 목록을 조회하면") {
            val result = productQueryService.findProducts(
                brandId = null,
                sortBy = sortBy,
                pageCriteria = PageCriteria(0, 20)
            ).content

            Then("가격 낮은 순으로 정렬된다") {
                result shouldHaveSize 5
                result[0].product.id shouldBe product5.id
                result[1].product.id shouldBe product4.id
                result[2].product.id shouldBe product1.id
                result[3].product.id shouldBe product3.id
                result[4].product.id shouldBe product2.id
            }
        }
    }

    Given("페이징을 적용하는 경우") {
        val product1 = productJpaRepository.save(createProduct())
        val product2 = productJpaRepository.save(createProduct())
        val product3 = productJpaRepository.save(createProduct())
        val product4 = productJpaRepository.save(createProduct())
        val product5 = productJpaRepository.save(createProduct())

        When("상품 목록을 조회하면") {
            // 두 번째 페이지, 페이지 사이즈 2
            val result = productQueryService.findProducts(
                brandId = null,
                sortBy = "latest",
                pageCriteria = PageCriteria(1, 2)
            ).content

            Then("페이징이 적용된다") {
                result shouldHaveSize 2
                result[0].product.id shouldBe product3.id
                result[1].product.id shouldBe product2.id
            }
        }
    }

    Given("브랜드 식별자로 조회하는 경우") {
        productJpaRepository.save(createProduct(brandId = 1L))
        productJpaRepository.save(createProduct(brandId = 1L))
        productJpaRepository.save(createProduct(brandId = 1L))
        productJpaRepository.save(createProduct(brandId = 99L))
        productJpaRepository.save(createProduct(brandId = 99L))

        When("상품 목록을 조회하면") {
            val result = productQueryService.findProducts(
                brandId = 99L,
                sortBy = "latest",
                pageCriteria = PageCriteria(0, 20)
            ).content

            Then("해당 브랜드의 상품이 조회된다") {
                result shouldHaveSize 2
                result.map { it.product }.forAll { it.brandId shouldBe 99L }
            }
        }
    }
})
