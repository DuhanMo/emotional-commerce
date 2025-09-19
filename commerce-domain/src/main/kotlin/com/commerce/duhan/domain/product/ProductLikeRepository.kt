package com.commerce.duhan.domain.product

interface ProductLikeRepository {
    fun findByUserIdAndProductId(userId: Long, productId: Long): ProductLike?

    fun save(productLike: ProductLike): ProductLike
}
