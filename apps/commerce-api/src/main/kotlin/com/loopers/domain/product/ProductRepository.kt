package com.loopers.domain.product

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductRepository {
    fun findAllProductSummary(
        brandId: Long?,
        sortBy: String,
        pageable: Pageable,
    ): Page<ProductWithSummaryInfo>

    fun getById(
        productId: Long,
    ): ProductWithSummaryInfo

    fun findAllProductSummaryById(
        productIds: List<Long>,
    ): List<ProductWithSummaryInfo>

    fun findAllById(
        productIds: List<Long>,
    ): List<Product>

    fun save(product: Product): Product
}
