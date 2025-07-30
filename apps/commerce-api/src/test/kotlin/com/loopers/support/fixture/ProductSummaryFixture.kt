package com.loopers.support.fixture

import com.loopers.domain.product.ProductSummary

fun createProductSummary(
    productId: Long,
    likeCount: Int = 100,
): ProductSummary = ProductSummary(
    productId = productId,
    likeCount = likeCount,
)
