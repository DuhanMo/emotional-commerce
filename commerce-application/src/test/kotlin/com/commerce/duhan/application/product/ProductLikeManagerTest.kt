package com.commerce.duhan.application.product

import com.commerce.duhan.domain.fixtures.createProduct
import com.commerce.duhan.domain.fixtures.createProductLike
import com.commerce.duhan.domain.product.ProductLikeRepository
import com.commerce.duhan.domain.product.ProductLikeStatus
import com.commerce.duhan.domain.product.ProductRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class ProductLikeManagerTest : DescribeSpec({
    val productLikeRepository = mockk<ProductLikeRepository>()
    val productRepository = mockk<ProductRepository>()
    val underTest = ProductLikeManager(productLikeRepository, productRepository)

    describe("상품 좋아요 추가") {
        context("상품 좋아요가 활성 상태로 존재하는 경우") {
            it("새로운 상품 좋아요를 저장하지 않는다") {
                // given
                every { productLikeRepository.findByUserIdAndProductId(any(), any()) } returns createProductLike()
                every { productRepository.getByProductId(any()) } returns createProduct()

                // when
                underTest.addLike(userId = 1, productId = 1)

                // then
                verify(exactly = 0) { productLikeRepository.save(any()) }
            }
        }

        context("상품 좋아요가 삭제 상태로 존재하는 경우") {
            it("기존 상품 좋아요를 활성상태로 갱신한다") {
                // given
                val productLike = createProductLike(status = ProductLikeStatus.DELETED)
                every { productLikeRepository.findByUserIdAndProductId(any(), any()) } returns productLike
                every { productLikeRepository.save(any()) } returns productLike
                every { productRepository.getByProductId(any()) } returns createProduct()

                // when
                underTest.addLike(userId = 1, productId = 1)

                // then
                productLike.status shouldBe ProductLikeStatus.ACTIVE
            }

            it("좋아요 수가 증가한다") {
                // given
                val productLike = createProductLike(status = ProductLikeStatus.DELETED)
                val product = createProduct(likeCount = 10)

                every { productLikeRepository.findByUserIdAndProductId(any(), any()) } returns
                        createProductLike(status = ProductLikeStatus.DELETED)
                every { productLikeRepository.save(any()) } returns productLike
                every { productRepository.getByProductId(any()) } returns product

                // when
                underTest.addLike(userId = 1, productId = 1)

                // then
                product.likeCount shouldBe 11
            }
        }

        context("상품 좋아요가 없는 경우") {
            it("새로운 상품 좋아요를 저장한다") {
                // given
                every { productLikeRepository.findByUserIdAndProductId(any(), any()) } returns null
                every { productLikeRepository.save(any()) } returns createProductLike()
                every { productRepository.getByProductId(any()) } returns createProduct()

                // when
                underTest.addLike(userId = 1, productId = 1)

                // then
                verify(exactly = 1) { productLikeRepository.save(any()) }
            }
            it("상품의 좋아요 수가 증가한다") {
                // given
                val product = createProduct(likeCount = 9)
                every { productLikeRepository.findByUserIdAndProductId(any(), any()) } returns null
                every { productLikeRepository.save(any()) } returns createProductLike()
                every { productRepository.getByProductId(any()) } returns product

                // when
                underTest.addLike(userId = 1, productId = 1)

                // then
                product.likeCount shouldBe 10
            }
        }
    }

    describe("상품 좋아요 삭제") {
        context("상품 좋아요가 활성 상태로 존재하는 경우") {
            it("상품 좋아요를 삭제 상태로 변경한다") {
                // given
                val productLike = createProductLike(status = ProductLikeStatus.ACTIVE)
                val product = createProduct(likeCount = 5)

                every { productLikeRepository.findByUserIdAndProductId(any(), any()) } returns productLike
                every { productRepository.getByProductId(any()) } returns product

                // when
                underTest.removeLike(userId = 1, productId = 1)

                // then
                productLike.status shouldBe ProductLikeStatus.DELETED
            }

            it("상품의 좋아요 수가 감소한다") {
                // given
                val productLike = createProductLike(status = ProductLikeStatus.ACTIVE)
                val product = createProduct(likeCount = 5)

                every { productLikeRepository.findByUserIdAndProductId(any(), any()) } returns productLike
                every { productRepository.getByProductId(any()) } returns product

                // when
                underTest.removeLike(userId = 1, productId = 1)

                // then
                product.likeCount shouldBe 4
            }
        }

        context("상품 좋아요가 없는 경우") {
            it("상품의 좋아요 수가 변경되지 않는다") {
                // given
                val product = createProduct(likeCount = 3)

                every { productLikeRepository.findByUserIdAndProductId(any(), any()) } returns null
                every { productRepository.getByProductId(any()) } returns product

                // when
                underTest.removeLike(userId = 1, productId = 1)

                // then
                product.likeCount shouldBe 3
            }
        }

        context("상품 좋아요가 삭제 상태로 존재하는 경우") {
            it("상품의 좋아요 수가 변경되지 않는다") {
                // given
                val productLike = createProductLike(status = ProductLikeStatus.DELETED)
                val product = createProduct(likeCount = 7)

                every { productLikeRepository.findByUserIdAndProductId(any(), any()) } returns productLike
                every { productRepository.getByProductId(any()) } returns product

                // when
                underTest.removeLike(userId = 1, productId = 1)

                // then
                product.likeCount shouldBe 7
            }
        }
    }

    afterTest {
        clearAllMocks()
    }
})
