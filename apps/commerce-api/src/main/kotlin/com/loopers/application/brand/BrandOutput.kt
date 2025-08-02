package com.loopers.application.brand

import com.loopers.domain.brand.Brand

data class BrandOutput(
    val id: Long,
    val name: String,
    val description: String?,
    val logoUrl: String?,
) {
    companion object {
        fun from(brand: Brand): BrandOutput = BrandOutput(
            id = brand.id,
            name = brand.name,
            description = brand.description,
            logoUrl = brand.logoUrl,
        )
    }
}
