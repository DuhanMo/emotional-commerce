package com.loopers.infrastructure.product

import com.loopers.domain.product.ProductLike
import com.loopers.domain.product.ProductLikeRepository
import com.loopers.domain.product.ProductLikeStatus.ACTIVE
import org.springframework.stereotype.Repository

@Repository
class ProductLikeRepositoryImpl(
    private val productLikeJpaRepository: ProductLikeJpaRepository,
) : ProductLikeRepository {
    override fun findByProductIdAndUserId(
        productId: Long,
        userId: Long,
    ): ProductLike? = productLikeJpaRepository.findByProductIdAndUserId(productId, userId)

    override fun save(productLike: ProductLike): ProductLike =
        productLikeJpaRepository.save(productLike)

    override fun findAllByUserId(userId: Long): List<ProductLike> =
        productLikeJpaRepository.findAllByUserIdAndStatusOrderByIdDesc(userId, ACTIVE)
}
