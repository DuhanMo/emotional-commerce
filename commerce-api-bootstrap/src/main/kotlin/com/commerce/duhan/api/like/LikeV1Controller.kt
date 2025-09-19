package com.commerce.duhan.api.like

import com.commerce.duhan.api.support.ApiResponse
import com.commerce.duhan.application.product.ProductApplicationService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/like")
class LikeV1Controller(
    private val productService: ProductApplicationService,
) {
    @PostMapping("/products/{productId}")
    fun addProductLike(
        @PathVariable productId: Long,
        @RequestHeader("X-USER-ID") userId: Long,
    ): ApiResponse<Any> {
        productService.addLike(userId, productId)
        return ApiResponse.success()
    }

    @DeleteMapping("/products/{productId}")
    fun removeProductLike(
        @PathVariable productId: Long,
        @RequestHeader("X-USER-ID") userId: Long,
    ): ApiResponse<Any> {
        productService.removeLike(userId, productId)
        return ApiResponse.success()
    }
}