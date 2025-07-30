package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "product_summary")
@Entity
class ProductSummary(
    val productId: Long,
    val likeCount: Int,
) : BaseEntity()
