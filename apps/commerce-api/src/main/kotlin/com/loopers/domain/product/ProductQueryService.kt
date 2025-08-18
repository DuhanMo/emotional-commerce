package com.loopers.domain.product

import com.loopers.domain.support.PageCriteria
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class ProductQueryService(
    private val productRepository: ProductRepository,
) {
    fun findAllProduct(
        brandId: Long?,
        sortBy: String,
        pageCriteria: PageCriteria,
    ): Page<Product> =
        productRepository.findAllProduct(
            brandId = brandId,
            sortBy = sortBy,
            pageable = pageCriteria.pageable,
        )

    fun getByIdWithSummary(productId: Long): ProductWithSummaryInfo = productRepository.getByIdWithSummary(productId)

    fun findAllProductSummaryById(productIds: List<Long>): List<ProductWithSummaryInfo> =
        productRepository.findAllProductSummaryById(productIds)
}
