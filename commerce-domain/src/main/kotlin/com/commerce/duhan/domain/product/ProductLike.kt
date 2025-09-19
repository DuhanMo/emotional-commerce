package com.commerce.duhan.domain.product

import com.commerce.duhan.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "product_like")
@Entity
class ProductLike(
    val userId: Long,
    val productId: Long,
    @Enumerated(EnumType.STRING)
    var status: ProductLikeStatus = ProductLikeStatus.ACTIVE,
) : BaseEntity() {
    val isActive: Boolean
        get() = status == ProductLikeStatus.ACTIVE

    val isDeleted: Boolean
        get() = status == ProductLikeStatus.DELETED

    fun active() {
        this.status = ProductLikeStatus.ACTIVE
        super.restore()
    }

    override fun delete() {
        this.status = ProductLikeStatus.DELETED
        super.delete()
    }
}
