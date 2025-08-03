package com.loopers.domain.product

import org.springframework.stereotype.Service

@Service
class ProductSummaryService(
    private val productSummaryRepository: ProductSummaryRepository,
) {
    fun increaseLikeCount(productId: Long) {
        val productSummary = productSummaryRepository.getByProductId(productId)

        productSummary.increaseLikeCount()

        productSummaryRepository.save(productSummary)
    }

    fun decreaseLikeCount(productId: Long) {
        val productSummary = productSummaryRepository.getByProductId(productId)

        productSummary.decreaseLikeCount()

        productSummaryRepository.save(productSummary)
    }
}
