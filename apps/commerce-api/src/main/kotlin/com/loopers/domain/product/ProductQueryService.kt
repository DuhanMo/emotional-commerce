package com.loopers.domain.product

import com.loopers.domain.support.PageCriteria
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class ProductQueryService(
    private val productRepository: ProductRepository,
) {
    fun findAllProductSummary(
        brandId: Long?,
        sortBy: String,
        pageCriteria: PageCriteria,
    ): Page<ProductWithSummaryInfo> =
        productRepository.findAllProductSummary(
            brandId = brandId,
            sortBy = sortBy,
            pageable = pageCriteria.pageable,
        )

    fun getById(productId: Long): ProductWithSummaryInfo = productRepository.getById(productId)

    fun findAllProductSummaryById(productIds: List<Long>): List<ProductWithSummaryInfo> =
        productRepository.findAllProductSummaryById(productIds)
}
