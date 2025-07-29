package com.loopers.domain.product

import com.loopers.domain.support.PageCriteria
import com.loopers.infrastructure.product.ProductJpaRepository
import com.loopers.infrastructure.product.ProductSummaryJpaRepository
import com.loopers.support.fixture.createProduct
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class ProductQueryServiceIGTest(
    private val productQueryService: ProductQueryService,
    private val productJpaRepository: ProductJpaRepository,
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
) : IntegrationSpec({
    Given("좋아요 많은 순으로 정렬하는 경우") {
        val sortBy = "likes_desc"

        val product1 = createProduct()
        val product2 = createProduct()
        val product3 = createProduct()
        val product4 = createProduct()
        val product5 = createProduct()
        productJpaRepository.saveAll(listOf(product1, product2, product3, product4, product5))
        productSummaryJpaRepository.saveAll(
            listOf(
                ProductSummary(product1.id, 1),
                ProductSummary(product2.id, 2),
                ProductSummary(product3.id, 20),
                ProductSummary(product4.id, 4),
                ProductSummary(product5.id, 5),
            )
        )

        When("상품 목록을 조회하면") {
            val products = productQueryService.findProducts(sortBy, PageCriteria(0, 20))

            Then("좋아요 순으로 정렬된다") {
                products shouldHaveSize 5
                products[0].id shouldBe product3.id
                products[1].id shouldBe product5.id
                products[2].id shouldBe product4.id
                products[3].id shouldBe product2.id
                products[4].id shouldBe product1.id
            }
        }
    }

    Given("가격 낮은 순으로 정렬하는 경우") {
        val sortBy = "price_asc"

        val product1 = createProduct(price = 1_000)
        val product2 = createProduct(price = 5_000)
        val product3 = createProduct(price = 2_000)
        val product4 = createProduct(price = 500)
        val product5 = createProduct(price = 100)
        productJpaRepository.saveAll(listOf(product1, product2, product3, product4, product5))

        When("상품 목록을 조회하면") {
            val products = productQueryService.findProducts(sortBy, PageCriteria(0, 20))

            Then("가격 낮은 순으로 정렬된다") {
                products shouldHaveSize 5
                products[0].id shouldBe product5.id
                products[1].id shouldBe product4.id
                products[2].id shouldBe product1.id
                products[3].id shouldBe product3.id
                products[4].id shouldBe product2.id
            }
        }
    }

    Given("페이징을 적용하는 경우") {
        val product1 = createProduct()
        val product2 = createProduct()
        val product3 = createProduct()
        val product4 = createProduct()
        val product5 = createProduct()
        productJpaRepository.saveAll(listOf(product1, product2, product3, product4, product5))

        When("상품 목록을 조회하면") {
            // 두 번째 페이지, 페이지 사이즈 2
            val products = productQueryService.findProducts("latest", PageCriteria(1, 2))

            Then("페이징이 적용된다") {
                products shouldHaveSize 2
                products[0].id shouldBe product3.id
                products[1].id shouldBe product2.id
            }
        }
    }
})