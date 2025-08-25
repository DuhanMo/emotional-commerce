package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "product_like")
@Entity
class ProductLike(
    val productId: Long,
    val userId: Long,
    @Enumerated(EnumType.STRING)
    var status: ProductLikeStatus = ProductLikeStatus.ACTIVE,
) : BaseEntity() {
    fun like() {
        this.status = ProductLikeStatus.ACTIVE
        restore()
    }

    fun unlike() {
        this.status = ProductLikeStatus.DELETED
        delete()
    }

    enum class ProductLikeStatus {
        ACTIVE,
        DELETED,
    }
}
