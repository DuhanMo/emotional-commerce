package com.loopers.interfaces.api.product

import com.loopers.application.product.ProductQueryFacade
import com.loopers.domain.support.PageCriteria
import com.loopers.interfaces.api.ApiResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/products")
class ProductV1Controller(
    private val productQueryFacade: ProductQueryFacade,
) : ProductV1ApiSpec {
    @GetMapping
    override fun getProducts(
        @RequestParam(defaultValue = "latest") sortBy: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): ApiResponse<ProductListResponse> {
        val output = productQueryFacade.findProducts(sortBy, PageCriteria(page, size))
        val response = ProductListResponse.from(output)
        return ApiResponse.success(response)
    }
}
