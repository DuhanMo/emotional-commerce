package com.loopers.application.product

import com.loopers.domain.product.ProductSummary

data class ProductSummaryOutput(
    val likeCount: Long,
) {
    companion object {
        fun from(summary: ProductSummary): ProductSummaryOutput = ProductSummaryOutput(
            likeCount = summary.likeCount,
        )
    }
}
