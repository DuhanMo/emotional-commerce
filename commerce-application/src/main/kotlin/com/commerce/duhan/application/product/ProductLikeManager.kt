package com.commerce.duhan.application.product

import com.commerce.duhan.domain.product.ProductLike
import com.commerce.duhan.domain.product.ProductLikeRepository
import com.commerce.duhan.domain.product.ProductRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ProductLikeManager(
    private val productLikeRepository: ProductLikeRepository,
    private val productRepository: ProductRepository,
) {
    @Transactional
    fun addLike(
        userId: Long,
        productId: Long,
    ) {
        val foundProductLike = productLikeRepository.findByUserIdAndProductId(userId, productId)
        val product = productRepository.getByProductId(productId)

        if (foundProductLike != null && foundProductLike.isActive) {
            return
        }

        val productLike = foundProductLike?.apply { active() } ?: ProductLike(userId, productId)

        product.addLike()
        productLikeRepository.save(productLike)
    }

    @Transactional
    fun removeLike(
        userId: Long,
        productId: Long,
    ) {
        val productLike = productLikeRepository.findByUserIdAndProductId(userId, productId)
        val product = productRepository.getByProductId(productId)

        if (productLike == null || productLike.isDeleted) {
            return
        }

        productLike.delete()
        product.removeLike()
    }
}
