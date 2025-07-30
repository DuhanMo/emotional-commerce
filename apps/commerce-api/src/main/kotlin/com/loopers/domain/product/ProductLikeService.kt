package com.loopers.domain.product

import org.springframework.stereotype.Service

@Service
class ProductLikeService(
    private val productLikeRepository: ProductLikeRepository,
) {
    fun likeProduct(
        productId: Long,
        userId: Long,
    ) {
        val productLike = findOrCreateProductLIke(productId, userId)

        productLike.like()

        productLikeRepository.save(productLike)
    }

    fun unlikeProduct(
        productId: Long,
        userId: Long,
    ) {
        val productLike = findOrCreateProductLIke(productId, userId)

        productLike.unlike()

        productLikeRepository.save(productLike)
    }

    private fun findOrCreateProductLIke(productId: Long, userId: Long): ProductLike =
        productLikeRepository.findByProductIdAndUserId(productId, userId)
            ?: ProductLike(productId, userId)
}
