package com.loopers.domain.product

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductRepository {
    fun findAllProduct(
        brandId: Long?,
        sortBy: String,
        pageable: Pageable,
    ): Page<Product>

    fun getByIdWithSummary(id: Long): ProductWithSummaryInfo

    fun findAllProductSummaryById(ids: List<Long>): List<ProductWithSummaryInfo>

    fun findAllById(ids: List<Long>): List<Product>

    fun save(product: Product): Product

    fun getByIdWithLock(id: Long): Product

    fun getById(id: Long): Product
}
