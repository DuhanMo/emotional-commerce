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
        pageCriteria: PageCriteria,
    ): Page<ProductInfo> =
        productRepository.findProducts(
            brandId = brandId,
            sortBy = sortBy,
            pageable = pageCriteria.pageable,
        )

    fun getById(productId: Long): ProductInfo = productRepository.getById(productId)

    fun findAllById(productIds: List<Long>): List<ProductInfo> =
        productRepository.findAllById(productIds)
}
