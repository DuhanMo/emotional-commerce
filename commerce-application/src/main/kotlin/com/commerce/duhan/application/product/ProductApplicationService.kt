package com.commerce.duhan.application.product

import org.springframework.stereotype.Service

@Service
class ProductApplicationService(
    private val productLikeManager: ProductLikeManager,
) {
    fun addLike(userId: Long, productId: Long) {
        productLikeManager.addLike(userId, productId)
    }

    fun removeLike(userId: Long, productId: Long) {
        productLikeManager.removeLike(userId, productId)
    }
}
