package com.loopers.application.product

import com.loopers.application.brand.BrandOutput
import com.loopers.domain.brand.Brand
import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductWithLikeCount
import com.loopers.domain.product.ProductWithSummaryInfo
import com.loopers.domain.support.Money

data class ProductItemOutput(
    val id: Long,
    val brandId: Long,
    val name: String,
    val description: String,
    val price: Money,
    val imageUrl: String?,
    val brand: BrandOutput,
    val summary: ProductSummaryOutput,
) {
    companion object {
        fun from(info: ProductWithSummaryInfo, brand: Brand): ProductItemOutput {
            val product = info.product
            return ProductItemOutput(
                id = product.id,
                brandId = product.brandId,
                name = product.name,
                description = product.description,
                price = product.price,
                imageUrl = product.imageUrl,
                brand = BrandOutput.from(brand),
                summary = ProductSummaryOutput.from(info.summary),
            )
        }

        fun from(info: ProductWithLikeCount, brand: Brand): ProductItemOutput {
            val product = info.product
            return ProductItemOutput(
                id = product.id,
                brandId = product.brandId,
                name = product.name,
                description = product.description,
                price = product.price,
                imageUrl = product.imageUrl,
                brand = BrandOutput.from(brand),
                summary = ProductSummaryOutput(info.likeCount),
            )
        }

        fun from(product: Product, brand: Brand): ProductItemOutput {
            return ProductItemOutput(
                id = product.id,
                brandId = product.brandId,
                name = product.name,
                description = product.description,
                price = product.price,
                imageUrl = product.imageUrl,
                brand = BrandOutput.from(brand),
                summary = ProductSummaryOutput(product.likeCount),
            )
        }
    }
}
