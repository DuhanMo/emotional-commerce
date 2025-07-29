package com.loopers.domain.product

import org.springframework.data.domain.Pageable

interface ProductRepository {
    fun findProducts(sortBy: String, pageable: Pageable): List<ProductQueryResult>
}