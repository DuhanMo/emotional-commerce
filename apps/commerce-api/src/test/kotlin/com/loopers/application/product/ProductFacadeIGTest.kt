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
        val user = userJpaRepository.save(createUser(LoginId("user123")))
        val product = productJpaRepository.save(createProduct())
        productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 0L))

        When("좋아요 등록 하면") {
            productFacade.likeProduct(LikeProductInput(product.id, LoginId("user123")))

            Then("상품 좋아요 데이터가 생성되고 상품 집계 좋아요 수가 증가한다") {
                val foundProductLike = productLikeJpaRepository.findAll()
                    .first { it.productId == product.id && it.userId == user.id }
                foundProductLike.status shouldBe ACTIVE

                val foundProductSummary = productSummaryJpaRepository.findByProductId(product.id)!!
                foundProductSummary.likeCount shouldBe 1L
            }
        }
    }

    Given("상품 좋아요 등록을 하지 않은 상태에서 좋아요를 취소하는 경우") {
        val user = userJpaRepository.save(createUser(LoginId("user123")))

        val product = productJpaRepository.save(createProduct())
        productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 0L))

        When("좋아요를 취소 하면") {
            productFacade.unlikeProduct(UnlikeProductInput(product.id, LoginId("user123")))

            Then("상품 좋아요 데이터가 생기지 않고 상품 집계 좋아요 수가 변하지 않는다") {
                val foundProductLike = productLikeJpaRepository.findAll()
                    .find { it.productId == product.id && it.userId == user.id }
                foundProductLike shouldBe null

                val foundProductSummary = productSummaryJpaRepository.findByProductId(product.id)!!
                foundProductSummary.likeCount shouldBe 0L
            }
        }
    }

    Given("좋아요 취소한 상품이 존재하는 경우") {
        val user = userJpaRepository.save(createUser(loginId = LoginId("user456")))
        val product = productJpaRepository.save(createProduct())
        productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 10L))
        productLikeJpaRepository.save(ProductLike(product.id, user.id, DELETED))

        When("좋아요 등록 하면") {
            productFacade.likeProduct(LikeProductInput(product.id, LoginId("user456")))

            Then("상품 좋아요 상태가 활성화 되고 상품 집계 좋아요 수가 증가한다") {
                val foundProductLike = productLikeJpaRepository.findAll()
                    .first { it.productId == product.id && it.userId == user.id }
                foundProductLike.status shouldBe ACTIVE

                val foundProductSummary = productSummaryJpaRepository.findByProductId(product.id)!!
                foundProductSummary.likeCount shouldBe 11L
            }
        }
    }

    Given("좋아요 등록된 상품이 존재하고 다른 사용자가 좋아요 등록 하는 경우") {
        val user1 = userJpaRepository.save(createUser(LoginId("user111")))
        val product = productJpaRepository.save(createProduct())
        productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 5L))
        productLikeJpaRepository.save(ProductLike(product.id, user1.id, ACTIVE))

        val user2 = userJpaRepository.save(createUser(LoginId("user222")))
        When("다른 사용자가 좋아요 등록하면") {
            productFacade.likeProduct(LikeProductInput(product.id, LoginId("user222")))

            Then("상품 집계 좋아요 수가 증가한다") {
                val foundProductSummary = productSummaryJpaRepository.findByProductId(product.id)!!
                foundProductSummary.likeCount shouldBe 6L

                val foundProductLike = productLikeJpaRepository.findAll()
                    .first { it.productId == product.id && it.userId == user2.id }
                foundProductLike.status shouldBe ACTIVE
            }
        }
    }
    Given("좋아요 등록된 상품이 존재하고 다른 사용자가 좋아요 취소 하는 경우") {
        val user1 = userJpaRepository.save(createUser(LoginId("user111")))
        val product = productJpaRepository.save(createProduct())
        productSummaryJpaRepository.save(ProductSummary(productId = product.id, likeCount = 5L))
        productLikeJpaRepository.save(ProductLike(product.id, user1.id, ACTIVE))

        val user2 = userJpaRepository.save(createUser(LoginId("user222")))
        productLikeJpaRepository.save(ProductLike(product.id, user2.id, ACTIVE))
        When("다른 사용자가 좋아요 취소하면") {

            productFacade.unlikeProduct(UnlikeProductInput(product.id, LoginId("user222")))

            Then("상품 집계 좋아요 수가 감소한다") {
                val foundProductSummary = productSummaryJpaRepository.findByProductId(product.id)!!
                foundProductSummary.likeCount shouldBe 4L

                val foundProductLike = productLikeJpaRepository.findAll()
                    .first { it.productId == product.id && it.userId == user2.id }
                foundProductLike.status shouldBe DELETED
            }
        }
    }
})
