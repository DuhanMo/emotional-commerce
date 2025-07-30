package com.loopers.domain.product

import com.loopers.domain.support.PageCriteria
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class ProductQueryService(
    private val productRepository: ProductRepository,
) {
    fun findProducts(
        brandId: Long?,
        sortBy: String,
        pageCriteria: PageCriteria
    ): Page<ProductQueryResult> =
        productRepository.findProducts(
            brandId = brandId,
            sortBy = sortBy,
            pageable = pageCriteria.pageable
        )
}
