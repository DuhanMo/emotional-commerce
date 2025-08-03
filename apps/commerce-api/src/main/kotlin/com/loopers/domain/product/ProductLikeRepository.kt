package com.loopers.domain.product

interface ProductLikeRepository {
    fun findByProductIdAndUserId(
        productId: Long,
        userId: Long,
    ): ProductLike?

    fun save(productLike: ProductLike): ProductLike

    fun findAllByUserId(userId: Long): List<ProductLike>
}
