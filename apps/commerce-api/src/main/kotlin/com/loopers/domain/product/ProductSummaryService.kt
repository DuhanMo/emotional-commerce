package com.loopers.domain.product

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductSummaryService(
    private val productSummaryRepository: ProductSummaryRepository,
) {
    @Transactional
    fun increaseLikeCount(productId: Long) {
        val productSummary = productSummaryRepository.getByProductId(productId)

        productSummary.increaseLikeCount()

        productSummaryRepository.save(productSummary)
    }

    @Transactional
    fun decreaseLikeCount(productId: Long) {
        val productSummary = productSummaryRepository.getByProductId(productId)

        productSummary.decreaseLikeCount()

        productSummaryRepository.save(productSummary)
    }
}
