package com.commerce.duhan.db.product

import com.commerce.duhan.domain.product.ProductLike
import com.commerce.duhan.domain.product.ProductLikeRepository
import org.springframework.stereotype.Repository

@Repository
class ProductLikeRepositoryImpl(
    private val jpaRepository: ProductLikeJpaRepository,
) : ProductLikeRepository {
    override fun findByUserIdAndProductId(userId: Long, productId: Long): ProductLike? =
        jpaRepository.findByUserIdAndProductId(userId, productId)

    override fun save(productLike: ProductLike): ProductLike = jpaRepository.save(productLike)
}
