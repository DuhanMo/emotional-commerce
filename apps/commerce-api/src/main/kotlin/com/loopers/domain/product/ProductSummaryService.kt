package com.loopers.domain.product

import org.springframework.stereotype.Service

@Service
class ProductSummaryService(
    private val productSummaryRepository: ProductSummaryRepository,
) {
    fun increaseLikeCount(productLike: ProductLike?) {
        if (productLike == null) {
            return
        }

        val productSummary = productSummaryRepository.getByProductId(productLike.productId)

        productSummary.increaseLikeCount()

        productSummaryRepository.save(productSummary)
    }

    fun decreaseLikeCount(productLike: ProductLike?) {
        if (productLike == null) {
            return
        }

        val productSummary = productSummaryRepository.getByProductId(productLike.productId)

        productSummary.decreaseLikeCount()

        productSummaryRepository.save(productSummary)
    }
}
