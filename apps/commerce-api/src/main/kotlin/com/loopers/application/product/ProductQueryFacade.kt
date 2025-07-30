package com.loopers.application.product

import com.loopers.domain.brand.BrandQueryService
import com.loopers.domain.product.ProductQueryService
import com.loopers.domain.support.PageCriteria
import org.springframework.stereotype.Service

@Service
class ProductQueryFacade(
    private val productQueryService: ProductQueryService,
    private val brandQueryService: BrandQueryService,
) {
    fun findProducts(
        brandId: Long?,
        sortBy: String,
        pageCriteria: PageCriteria,
    ): ProductListOutput {
        val productPage = productQueryService.findProducts(
            brandId = brandId,
            sortBy = sortBy,
            pageCriteria = pageCriteria
        )

        val brandIds = productPage.content.map { it.product.brandId }.distinct()
        val brands = brandQueryService.findBrands(brandIds)

        return ProductListOutput.from(productPage, brands)
    }
}
