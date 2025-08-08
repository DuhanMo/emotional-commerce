package com.loopers.domain.product

import com.loopers.domain.support.PageCriteria
import com.loopers.infrastructure.product.ProductJpaRepository
import com.loopers.infrastructure.product.ProductSummaryJpaRepository
import com.loopers.support.fixture.createProduct
import com.loopers.support.tests.IntegrationSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldBeSortedBy
import io.kotest.matchers.collections.shouldBeSortedDescendingBy
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class ProductQueryServiceIGTest(
    private val productQueryService: ProductQueryService,
    private val productJpaRepository: ProductJpaRepository,
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
) : IntegrationSpec({
    fun setupData() {
        val product1 = productJpaRepository.save(createProduct(brandId = 1L, price = 10_000))
        val product2 = productJpaRepository.save(createProduct(brandId = 1L, price = 150_000))
        val product3 = productJpaRepository.save(createProduct(brandId = 1L, price = 5_000))
        val product4 = productJpaRepository.save(createProduct(brandId = 99L, price = 500))
        val product5 = productJpaRepository.save(createProduct(brandId = 99L, price = 20_000))

        productSummaryJpaRepository.save(ProductSummary(productId = product1.id, likeCount = 100L))
        productSummaryJpaRepository.save(ProductSummary(productId = product2.id, likeCount = 200L))
        productSummaryJpaRepository.save(ProductSummary(productId = product3.id, likeCount = 300L))
        productSummaryJpaRepository.save(ProductSummary(productId = product4.id, likeCount = 400L))
        productSummaryJpaRepository.save(ProductSummary(productId = product5.id, likeCount = 500L))
    }

    Given("좋아요 내림차순으로 정렬하는 경우") {
        setupData()
        val sortBy = "likes_desc"

        When("상품 목록을 조회하면") {
            val result = productQueryService.findAllProductSummary(
                brandId = null,
                sortBy = sortBy,
                pageCriteria = PageCriteria(0, 20),
            ).content

            Then("좋아요 내림차순으로 정렬된다") {
                result shouldHaveSize 5
                result.shouldBeSortedDescendingBy { it.summary.likeCount }
            }
        }
    }

    Given("가격 오름차순으로 정렬하는 경우") {
        setupData()
        val sortBy = "price_asc"

        When("상품 목록을 조회하면") {
            val result = productQueryService.findAllProductSummary(
                brandId = null,
                sortBy = sortBy,
                pageCriteria = PageCriteria(0, 20),
            ).content

            Then("가격 오름차순으로 정렬된다") {
                result shouldHaveSize 5
                result.shouldBeSortedBy { it.product.price }
            }
        }
    }

    Given("페이징을 적용하는 경우") {
        setupData()

        When("상품 목록을 조회하면") {
            // 두 번째 페이지, 페이지 사이즈 2
            val result = productQueryService.findAllProductSummary(
                brandId = null,
                sortBy = "latest",
                pageCriteria = PageCriteria(1, 2),
            ).content

            Then("페이징이 적용된다") {
                result shouldHaveSize 2
            }
        }
    }

    Given("브랜드 식별자로 조회하는 경우") {
        setupData()
        val brandId = 99L

        When("상품 목록을 조회하면") {
            val result = productQueryService.findAllProductSummary(
                brandId = brandId,
                sortBy = "latest",
                pageCriteria = PageCriteria(0, 20),
            ).content

            Then("해당 브랜드의 상품이 조회된다") {
                result shouldHaveSize 2
                result.map { it.product }.forAll { it.brandId shouldBe brandId }
            }
        }
    }
})
