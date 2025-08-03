package com.loopers.domain.product

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductLikeService(
    private val productLikeRepository: ProductLikeRepository,
) {
    @Transactional
    fun likeProduct(
        productId: Long,
        userId: Long,
    ): ProductLike? {
        val productLike = productLikeRepository.findByProductIdAndUserId(productId, userId)
        if (productLike != null && productLike.status == ProductLikeStatus.ACTIVE) {
            return null
        }

        val updateProductLike = productLike ?: ProductLike(productId, userId)
        updateProductLike.like()

        return productLikeRepository.save(updateProductLike)
    }

    @Transactional
    fun unlikeProduct(
        productId: Long,
        userId: Long,
    ): ProductLike? {
        val productLike = productLikeRepository.findByProductIdAndUserId(productId, userId) ?: return null
        if (productLike.status == ProductLikeStatus.DELETED) {
            return null
        }

        productLike.unlike()
        return productLikeRepository.save(productLike)
    }
}