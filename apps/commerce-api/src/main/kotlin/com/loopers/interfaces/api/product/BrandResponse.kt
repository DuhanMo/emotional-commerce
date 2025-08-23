package com.loopers.interfaces.api.product

import com.loopers.application.brand.BrandOutput
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "브랜드 정보")
data class BrandResponse(
    @field:Schema(description = "브랜드 식별자", example = "14703")
    val id: Long,

    @field:Schema(description = "브랜드명", example = "핀카")
    val name: String,

    @field:Schema(description = "브랜드 설명", example = "편안하고 실용적인 디자인을 추구합니다.")
    val description: String?,

    @field:Schema(description = "브랜드 로고 URL", example = "/brand/logo.png")
    val logoUrl: String?,
) {
    companion object {
        fun from(output: BrandOutput): BrandResponse = BrandResponse(
            id = output.id,
            name = output.name,
            description = output.description,
            logoUrl = output.logoUrl,
        )
    }
}
