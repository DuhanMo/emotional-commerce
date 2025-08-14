package com.loopers.domain.product

import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductSummaryService(
    private val productSummaryRepository: ProductSummaryRepository,
) {
    @Async
    @Transactional
    @Retryable(
        value = [OptimisticLockingFailureException::class],
        maxAttempts = 5,
        backoff = Backoff(delay = 50, multiplier = 2.0),
    )
    fun increaseLikeCount(productId: Long) {
        val productSummary = productSummaryRepository.getByProductId(productId)

        productSummary.increaseLikeCount()

        productSummaryRepository.save(productSummary)
    }

    @Async
    @Transactional
    @Retryable(
        value = [OptimisticLockingFailureException::class],
        maxAttempts = 5,
        backoff = Backoff(delay = 50, multiplier = 2.0),
    )
    fun decreaseLikeCount(productId: Long) {
        val productSummary = productSummaryRepository.getByProductId(productId)

        productSummary.decreaseLikeCount()

        productSummaryRepository.save(productSummary)
    }
}
