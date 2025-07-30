package com.loopers.application.product

import com.loopers.domain.product.ProductSummary

data class ProductSummaryOutput(
    val likeCount: Int,
) {
    companion object {
        fun from(summary: ProductSummary?): ProductSummaryOutput = ProductSummaryOutput(
            likeCount = summary?.likeCount ?: 0,
        )
    }
}
