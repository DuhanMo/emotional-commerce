package com.loopers.domain.product

import com.loopers.domain.support.PageCriteria
import org.springframework.stereotype.Service

@Service
class ProductQueryService(
    private val productRepository: ProductRepository,
) {
    fun findProducts(sortBy: String, pageCriteria: PageCriteria): List<Product> =
        productRepository.findProducts(sortBy, pageCriteria.pageable)
}