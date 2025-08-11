package com.loopers.infrastructure.batch

import com.loopers.domain.product.ProductLikeRepository
import com.loopers.domain.product.ProductSummaryRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Component

@Component
class ProductLikeCountSyncBatchJobService(
    private val productLikeRepository: ProductLikeRepository,
    private val productSummaryRepository: ProductSummaryRepository,
) {
    @Transactional
    fun syncProcess(productIds: List<Long>) {
        val productLikeCounts = productLikeRepository.findAllActiveCountByProductId(productIds)
            .associateBy { it.productId }
        val productSummaries =
            productSummaryRepository.findAllByProductId(productLikeCounts.keys.toList().filterNotNull())

        productSummaries.forEach { productSummary ->
            productLikeCounts[productSummary.productId]?.let { productLikeCount ->
                productSummary.updateLikeCount(productLikeCount.likeCount ?: 0)
                productSummaryRepository.save(productSummary)
            }
        }
    }
}
