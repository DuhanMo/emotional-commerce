package com.loopers.domain.product

interface ProductSummaryRepository {
    fun getByProductId(productId: Long): ProductSummary

    fun save(productSummary: ProductSummary): ProductSummary
}
