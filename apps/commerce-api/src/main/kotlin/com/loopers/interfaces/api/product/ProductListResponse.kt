package com.loopers.interfaces.api.product

import com.loopers.application.product.ProductListOutput
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "상품 목록 응답")
data class ProductListResponse(
    @field:Schema(description = "상품 목록")
    val products: List<ProductItemResponse>,

    @field:Schema(description = "전체 상품 수", example = "193518")
    val totalCount: Long,

    @field:Schema(description = "다음 페이지 존재 여부", example = "true")
    val hasMore: Boolean,
) {
    companion object {
        fun from(output: ProductListOutput): ProductListResponse = ProductListResponse(
            products = output.products.map { ProductItemResponse.from(it) },
            totalCount = output.totalCount,
            hasMore = output.hasMore,
        )
    }
}
