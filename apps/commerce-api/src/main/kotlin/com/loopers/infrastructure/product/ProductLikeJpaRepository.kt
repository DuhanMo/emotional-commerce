package com.loopers.infrastructure.product

import com.loopers.domain.product.ProductLike
import com.loopers.domain.product.ProductLikeStatus
import org.springframework.data.jpa.repository.JpaRepository

interface ProductLikeJpaRepository : JpaRepository<ProductLike, Long> {
    fun findByProductIdAndUserId(
        productId: Long,
        userId: Long,
    ): ProductLike?

    fun findAllByUserIdAndStatusOrderByIdDesc(
        userId: Long,
        status: ProductLikeStatus,
    ): List<ProductLike>
}
