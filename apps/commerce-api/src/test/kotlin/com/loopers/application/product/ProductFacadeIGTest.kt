package com.loopers.application.product

import com.loopers.domain.product.ProductLike
import com.loopers.domain.product.ProductLikeStatus.ACTIVE
import com.loopers.domain.product.ProductLikeStatus.DELETED
import com.loopers.domain.product.ProductSummary
import com.loopers.domain.user.LoginId
import com.loopers.infrastructure.product.ProductJpaRepository
import com.loopers.infrastructure.product.ProductLikeJpaRepository
import com.loopers.infrastructure.product.ProductSummaryJpaRepository
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.support.fixture.createProduct
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.shouldBe

class ProductFacadeIGTest(
    private val productFacade: ProductFacade,
    private val userJpaRepository: UserJpaRepository,
    private val productJpaRepository: ProductJpaRepository,
    private val productLikeJpaRepository: ProductLikeJpaRepository,
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
) : IntegrationSpec({

    Given("상품 좋아요가 없는 경우") {
        val user = userJpaRepository.save(createUser(loginId = LoginId("user123")))
        val product = productJpaRepository.save(createProduct())
        productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 0L))

        When("좋아요 등록 하면") {
            productFacade.likeProduct(LikeProductInput(productId = product.id, loginId = LoginId("user123")))

            Then("상품 좋아요 데이터가 생성되고 상품 집계 좋아요 수가 증가한다") {
                val foundProductLike = productLikeJpaRepository.findAll()
                    .first { it.productId == product.id && it.userId == user.id }
                foundProductLike.status shouldBe ACTIVE

                val foundProductSummary = productSummaryJpaRepository.findByProductId(product.id)!!
                foundProductSummary.likeCount shouldBe 1L
            }
        }
    }

    Given("좋아요 취소한 상품을 좋아요 등록하는 경우") {
        val user = userJpaRepository.save(createUser(loginId = LoginId("user456")))
        val product = productJpaRepository.save(createProduct())
        productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 10L))
        productLikeJpaRepository.save(ProductLike(productId = product.id, userId = user.id, status = DELETED))

        When("좋아요 등록 하면") {
            productFacade.likeProduct(LikeProductInput(productId = product.id, loginId = LoginId("user456")))

            Then("상품 좋아요 상태가 활성화 되고 상품 집계 좋아요 수가 증가한다") {
                val foundProductLike = productLikeJpaRepository.findAll()
                    .first { it.productId == product.id && it.userId == user.id }
                foundProductLike.status shouldBe ACTIVE

                val foundProductSummary = productSummaryJpaRepository.findByProductId(product.id)!!
                foundProductSummary.likeCount shouldBe 11L
            }
        }
    }

    Given("좋아요한 상품을") {
        val user1 = userJpaRepository.save(createUser(loginId = LoginId("user111")))
        val user2 = userJpaRepository.save(createUser(loginId = LoginId("user222")))
        val product = productJpaRepository.save(createProduct())
        productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 5L))
        productLikeJpaRepository.save(ProductLike(productId = product.id, userId = user1.id, status = ACTIVE))

        When("다른 사용자가 좋아요 등록하면") {
            productFacade.likeProduct(LikeProductInput(productId = product.id, loginId = LoginId("user222")))

            Then("상품 집계 좋아요 수가 증가한다") {
                val foundProductSummary = productSummaryJpaRepository.findByProductId(product.id)!!
                foundProductSummary.likeCount shouldBe 6L

                val foundProductLike = productLikeJpaRepository.findAll()
                    .first { it.productId == product.id && it.userId == user2.id }
                foundProductLike.status shouldBe ACTIVE
            }
        }
    }
})
