package com.loopers.interfaces.api.product

import com.loopers.application.product.ProductSummaryOutput
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "상품 요약 정보")
data class ProductSummaryResponse(
    @field:Schema(description = "좋아요 수", example = "13999")
    val likeCount: Long,
) {
    companion object {
        fun from(output: ProductSummaryOutput): ProductSummaryResponse = ProductSummaryResponse(
            likeCount = output.likeCount,
        )
    }
}
