package com.loopers.domain.product

data class ProductWithLikeCount(
    val product: Product,
    val likeCount: Long,
)
