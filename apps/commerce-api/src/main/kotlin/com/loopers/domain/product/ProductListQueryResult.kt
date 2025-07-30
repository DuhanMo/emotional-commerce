package com.loopers.domain.product

data class ProductListQueryResult(
    val product: Product,
    val summary: ProductSummary?,
)
