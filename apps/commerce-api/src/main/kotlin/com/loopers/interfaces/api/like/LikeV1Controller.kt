package com.loopers.interfaces.api.like

import com.loopers.application.product.LikeProductInput
import com.loopers.application.product.ProductFacade
import com.loopers.application.product.ProductQueryFacade
import com.loopers.application.product.UnlikeProductInput
import com.loopers.domain.user.LoginId
import com.loopers.interfaces.api.ApiResponse
import com.loopers.interfaces.api.product.ProductItemResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/like")
class LikeV1Controller(
    private val productFacade: ProductFacade,
    private val productQueryFacade: ProductQueryFacade,
) {
    @PostMapping("/products/{productId}")
    fun likeProduct(
        @PathVariable productId: Long,
        @RequestHeader("X-USER-ID") loginId: String,
    ): ApiResponse<Any> {
        productFacade.likeProduct(LikeProductInput(productId, LoginId(loginId)))
        return ApiResponse.success()
    }

    @DeleteMapping("/products/{productId}")
    fun unlikeProduct(
        @PathVariable productId: Long,
        @RequestHeader("X-USER-ID") loginId: String,
    ): ApiResponse<Any> {
        productFacade.unlikeProduct(UnlikeProductInput(productId, LoginId(loginId)))
        return ApiResponse.success()
    }

    @GetMapping("/products")
    fun getLikedProducts(
        @RequestHeader("X-USER-ID") loginId: String,
    ): ApiResponse<List<ProductItemResponse>> {
        val response = productQueryFacade.findLikedProducts(LoginId(loginId))
            .map { output -> ProductItemResponse.from(output) }
        return ApiResponse.success(response)
    }
}
