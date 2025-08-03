package com.loopers.domain.product

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductRepository {
    fun findProducts(
        brandId: Long?,
        sortBy: String,
        pageable: Pageable,
    ): Page<ProductInfo>

    fun getById(
        productId: Long,
    ): ProductInfo

    fun findAllById(
        productIds: List<Long>,
    ): List<ProductInfo>

    fun save(product: Product): Product
}
