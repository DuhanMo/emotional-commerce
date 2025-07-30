package com.loopers.domain.product

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductLikeService(
    private val productLikeRepository: ProductLikeRepository,
) {
    fun likeProduct(
        productId: Long,
        userId: Long,
    ) {
        val productLike = findOrCreateProductLike(productId, userId)

        productLike.like()

        productLikeRepository.save(productLike)
    }

    @Transactional
    fun unlikeProduct(
        productId: Long,
        userId: Long,
    ) {
        val productLike = findOrCreateProductLike(productId, userId)

        productLike.unlike()

        productLikeRepository.save(productLike)
    }

    private fun findOrCreateProductLike(productId: Long, userId: Long): ProductLike =
        productLikeRepository.findByProductIdAndUserId(productId, userId)
            ?: ProductLike(productId, userId)
}
