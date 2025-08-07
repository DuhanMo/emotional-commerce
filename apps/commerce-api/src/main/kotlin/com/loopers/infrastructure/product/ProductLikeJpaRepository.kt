package com.loopers.infrastructure.product

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.loopers.domain.product.ProductLike
import com.loopers.domain.product.ProductLikeStatus
import java.time.ZonedDateTime
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProductLikeJpaRepository : JpaRepository<ProductLike, Long>, KotlinJdslJpqlExecutor {
    fun findByProductIdAndUserId(
        productId: Long,
        userId: Long,
    ): ProductLike?

    fun findAllByUserIdAndStatusOrderByIdDesc(
        userId: Long,
        status: ProductLikeStatus,
    ): List<ProductLike>

    @Query(
        """
        SELECT DISTINCT pl.productId 
        FROM ProductLike pl 
        WHERE pl.updatedAt >= :from
        AND pl.updatedAt < :to
        ORDER BY pl.productId
    """,
    )
    fun findLikeUpdatedProductIds(
        @Param("from") from: ZonedDateTime,
        @Param("to") to: ZonedDateTime,
        pageable: Pageable,
    ): Page<Long>
}
