package com.loopers.domain.product

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductRepository {
    fun findProducts(
        brandId: Long?,
        sortBy: String,
        pageable: Pageable
    ): Page<ProductQueryResult>
}
