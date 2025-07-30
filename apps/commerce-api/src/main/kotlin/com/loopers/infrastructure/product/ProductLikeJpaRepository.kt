package com.loopers.infrastructure.product

import com.loopers.domain.product.ProductLike
import org.springframework.data.jpa.repository.JpaRepository

interface ProductLikeJpaRepository : JpaRepository<ProductLike, Long> {
    fun findByProductIdAndUserId(
        productId: Long,
        userId: Long,
    ): ProductLike?
}
