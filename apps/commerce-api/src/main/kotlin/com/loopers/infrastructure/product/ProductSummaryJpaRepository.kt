package com.loopers.infrastructure.product

import com.loopers.domain.product.ProductSummary
import org.springframework.data.jpa.repository.JpaRepository

interface ProductSummaryJpaRepository : JpaRepository<ProductSummary, Long> {
    fun findByProductId(productId: Long): ProductSummary?

    fun findAllByProductIdIn(productIds: List<Long>): List<ProductSummary>
}
