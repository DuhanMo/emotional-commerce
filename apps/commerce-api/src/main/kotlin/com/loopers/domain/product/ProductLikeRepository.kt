package com.loopers.domain.product

import java.time.ZonedDateTime
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProductLikeRepository {
    fun findByProductIdAndUserId(
        productId: Long,
        userId: Long,
    ): ProductLike?

    fun save(productLike: ProductLike): ProductLike

    fun findAllByUserId(userId: Long): List<ProductLike>

    fun findAllLikeUpdatedProductId(
        from: ZonedDateTime,
        to: ZonedDateTime,
        pageable: Pageable,
    ): Page<Long>

    fun findAllActiveCountByProductId(productIds: List<Long>): List<ProductLikeCount>
}
