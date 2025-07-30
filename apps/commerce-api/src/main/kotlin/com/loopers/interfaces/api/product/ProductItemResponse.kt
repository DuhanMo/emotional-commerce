package com.loopers.interfaces.api.product

import com.loopers.application.product.ProductItemOutput
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "상품 정보")
data class ProductItemResponse(
    @Schema(description = "상품 ID", example = "1")
    val id: Long,

    @Schema(description = "브랜드 ID", example = "14703")
    val brandId: Long,

    @Schema(description = "상품명", example = "시티워커 나일론스판 쇼츠")
    val name: String,

    @Schema(description = "상품 설명", example = "편안한 착용감의 쇼츠입니다.")
    val description: String,

    @Schema(description = "가격", example = "36000")
    val price: Int,

    @Schema(description = "재고 수량", example = "100")
    val stock: Int,

    @Schema(description = "상품 이미지 URL", example = "/item/202506/image.png")
    val imageUrl: String?,

    @Schema(description = "브랜드 정보")
    val brand: BrandResponse,

    @Schema(description = "상품 요약 정보")
    val summary: ProductSummaryResponse,
) {
    companion object {
        fun from(output: ProductItemOutput): ProductItemResponse = ProductItemResponse(
            id = output.id,
            brandId = output.brandId,
            name = output.name,
            description = output.description,
            price = output.price,
            stock = output.stock,
            imageUrl = output.imageUrl,
            brand = BrandResponse.from(output.brand),
            summary = ProductSummaryResponse.from(output.summary),
        )
    }
}
