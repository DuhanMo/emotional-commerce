package com.loopers.domain.product

import com.loopers.infrastructure.product.ProductLikeJpaRepository
import com.loopers.infrastructure.product.ProductSummaryJpaRepository
import com.loopers.support.error.CoreException
import com.loopers.support.tests.IntegrationSpec
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

class ProductSummaryServiceIGTest(
    private val productSummaryService: ProductSummaryService,
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
    private val productLikeJpaRepository: ProductLikeJpaRepository,
) : IntegrationSpec({
    Given("상품 집계 좋아요 수가 0인 경우") {
        productSummaryJpaRepository.save(ProductSummary(productId = 1L, likeCount = 0L))

        When("상품 집계 좋아요 수 감소하면") {
            Then("예외 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    productSummaryService.decreaseLikeCount(ProductLike(productId = 1L, userId = 99L))
                }
            }
        }
    }

    Given("상품 집계가 존재하지 않는 경우") {
        When("상품 집계 좋아요 수 증가하면") {
            Then("예외 발생한다") {
                shouldThrow<CoreException> {
                    productSummaryService.increaseLikeCount(ProductLike(productId = 1L, userId = 99L))
                }
            }
        }
    }

    Given("상품 집계 좋아요 수를 증가하는 경우") {
        productSummaryJpaRepository.save(ProductSummary(productId = 1L, likeCount = 100L))

        When("상품 집계 좋아요 수 증가하면") {
            productSummaryService.increaseLikeCount(ProductLike(productId = 1L, userId = 99L))

            Then("좋아요 수 1 증가한다") {
                val foundProductSummary = productSummaryJpaRepository.findByProductId(1L)
                foundProductSummary!!.likeCount shouldBe 101L
            }
        }
    }

    Given("상품 집계 좋아요 수를 감소하는 경우") {
        productSummaryJpaRepository.save(ProductSummary(productId = 1L, likeCount = 100L))
        productLikeJpaRepository.save(ProductLike(productId = 99L, userId = 99L))

        When("상품 집계 좋아요 수 감소하면") {
            productSummaryService.decreaseLikeCount(ProductLike(productId = 1L, userId = 99L))

            Then("좋아요 수 1 감소한다") {
                val foundProductSummary = productSummaryJpaRepository.findByProductId(1L)
                foundProductSummary!!.likeCount shouldBe 99L
            }
        }
    }
})
