package com.loopers.application.product

import com.loopers.application.brand.BrandOutput
import com.loopers.domain.brand.Brand
import com.loopers.domain.product.ProductInfo

data class ProductItemOutput(
    val id: Long,
    val brandId: Long,
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int,
    val imageUrl: String?,
    val brand: BrandOutput,
    val summary: ProductSummaryOutput,
) {
    companion object {
        fun from(info: ProductInfo, brand: Brand): ProductItemOutput {
            val product = info.product
            return ProductItemOutput(
                id = product.id,
                brandId = product.brandId,
                name = product.name,
                description = product.description,
                price = product.price,
                stock = product.stock,
                imageUrl = product.imageUrl,
                brand = BrandOutput.from(brand),
                summary = ProductSummaryOutput.from(info.summary),
            )
        }
    }
}
