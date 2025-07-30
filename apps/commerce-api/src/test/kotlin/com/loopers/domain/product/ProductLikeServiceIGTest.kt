package com.loopers.domain.product

import com.loopers.domain.product.ProductLikeStatus.ACTIVE
import com.loopers.domain.product.ProductLikeStatus.DELETED
import com.loopers.infrastructure.product.ProductLikeJpaRepository
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ProductLikeServiceIGTest(
    private val productLikeService: ProductLikeService,
    private val productLikeJpaRepository: ProductLikeJpaRepository,
) : IntegrationSpec({
    Given("상품 좋아요가 존재하는 경우") {
        beforeEach {
            productLikeJpaRepository.save(ProductLike(productId = 1L, userId = 99L))
        }
        When("좋아요를 등록하면") {
            productLikeService.likeProduct(productId = 1L, userId = 99L)

            Then("상태가 활성 이다") {
                val foundProductLike =
                    productLikeJpaRepository.findAll().first { it.productId == 1L && it.userId == 99L }

                foundProductLike.status shouldBe ACTIVE
            }
        }
        When("좋아요를 취소하면") {
            productLikeService.unlikeProduct(productId = 1L, userId = 99L)

            Then("상태가 삭제 이다") {
                val foundProductLike =
                    productLikeJpaRepository.findAll().first { it.productId == 1L && it.userId == 99L }

                foundProductLike.status shouldBe DELETED
            }
        }
    }

    Given("상품 좋아요가 이미 활성 상태인 경우") {
        productLikeJpaRepository.save(ProductLike(productId = 1L, userId = 99L, status = ACTIVE))

        When("좋아요를 등록하면") {
            productLikeService.likeProduct(productId = 1L, userId = 99L)

            Then("상태가 활성 이다") {
                val foundProductLike =
                    productLikeJpaRepository.findAll().first { it.productId == 1L && it.userId == 99L }

                foundProductLike.status shouldBe ACTIVE
            }
        }
    }

    Given("상품 좋아요가 존재하지 않는 경우") {
        When("좋아요를 등록하면") {
            productLikeService.likeProduct(productId = 1L, userId = 99L)

            Then("새로운 상품 좋아요가 생성된다") {
                val foundProductLike = productLikeJpaRepository.findAll()
                    .firstOrNull { it.productId == 1L && it.userId == 99L }
                foundProductLike shouldNotBe null
                foundProductLike!!.status shouldBe ACTIVE
            }
        }
    }
})
