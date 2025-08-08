package com.loopers.infrastructure.batch

import com.loopers.domain.product.ProductLikeRepository
import com.loopers.domain.support.PageCriteria
import java.time.Instant
import java.time.temporal.ChronoUnit
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ProductLikeCountSyncBatchJob(
    private val service: ProductLikeCountSyncBatchJobService,
    private val productLikeRepository: ProductLikeRepository,
) {
    @Scheduled(fixedDelay = 3600000)
    fun syncProductLikeCounts() {
        val now = Instant.now()
        val twoHoursAgo = now.minus(2, ChronoUnit.HOURS)
        val batchSize = 200
        val pageCriteria = PageCriteria(0, batchSize)
        do {
            val page = productLikeRepository.findAllLikeUpdatedProductId(twoHoursAgo, now, pageCriteria.pageable)
            val likeUpdatedProductIds = page.content
            service.syncProcess(likeUpdatedProductIds)
        } while (page.hasNext())
    }
}
