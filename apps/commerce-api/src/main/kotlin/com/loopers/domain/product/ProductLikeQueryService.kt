package com.loopers.domain.product

import org.springframework.stereotype.Service

@Service
class ProductLikeQueryService(
    private val productLikeRepository: ProductLikeRepository,
) {
    fun findLikedProducts(userId: Long): List<ProductLike> = productLikeRepository.findAllByUserId(userId)
}
