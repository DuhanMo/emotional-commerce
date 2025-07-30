package com.loopers.application.product

import com.loopers.domain.brand.Brand
import com.loopers.domain.product.ProductQueryResult
import org.springframework.data.domain.Page

data class ProductListOutput(
    val products: List<ProductItemOutput>,
    val totalCount: Long,
    val hasMore: Boolean,
) {
    companion object {
        fun from(productPage: Page<ProductQueryResult>, brands: List<Brand>): ProductListOutput {
            val brandMap = brands.associateBy { it.id }

            val productItems = productPage.content.map { queryResult ->
                val brand = brandMap[queryResult.product.brandId]
                    ?: throw IllegalStateException("해당 상품의 브랜드를 찾을 수 없습니다.(productId: ${queryResult.product.id})")

                ProductItemOutput.from(queryResult, brand)
            }

            return ProductListOutput(
                products = productItems,
                totalCount = productPage.totalElements,
                hasMore = productPage.hasNext(),
            )
        }
    }
}
