package com.loopers.infrastructure.batch

import com.loopers.domain.product.ProductLike
import com.loopers.domain.product.ProductLikeStatus
import com.loopers.domain.product.ProductSummary
import com.loopers.infrastructure.product.ProductLikeJpaRepository
import com.loopers.infrastructure.product.ProductSummaryJpaRepository
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.shouldBe

class ProductLikeCountSyncBatchJobTest(
    private val productLikeCountSyncBatchJob: ProductLikeCountSyncBatchJob,
    private val productLikeJpaRepository: ProductLikeJpaRepository,
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
) : IntegrationSpec({
    Given("활성화된 상품 좋아요가 존재하는 경우") {
        productSummaryJpaRepository.save(ProductSummary(1L))
        productSummaryJpaRepository.save(ProductSummary(2L))
        // 상품1의 활성상태 상품 좋아요 저장
        repeat(100) { index ->
            productLikeJpaRepository.save(ProductLike(1L, index.toLong(), ProductLikeStatus.ACTIVE))
        }
        // 상품1의 삭제상태 상품 좋아요 저장
        repeat(50) { index ->
            productLikeJpaRepository.save(ProductLike(1L, index.toLong(), ProductLikeStatus.DELETED))
        }
        // 상품2의 활성상태 상품 좋아요 저장
        repeat(200) { index ->
            productLikeJpaRepository.save(ProductLike(2L, index.toLong(), ProductLikeStatus.ACTIVE))
        }
        // 상품2의 삭제상태 상품 좋아요 저장
        repeat(50) { index ->
            productLikeJpaRepository.save(ProductLike(2L, index.toLong(), ProductLikeStatus.DELETED))
        }

        When("상품 집계 좋아요 수 싱크 배치가 동작하면") {
            productLikeCountSyncBatchJob.syncProductLikeCounts()

            Then("상품 집계 좋아요 수가 갱신된다") {
                val summaries = productSummaryJpaRepository.findAll()
                summaries.first { it.productId == 1L }.likeCount shouldBe 100
                summaries.first { it.productId == 2L }.likeCount shouldBe 200
            }
        }
    }
})
