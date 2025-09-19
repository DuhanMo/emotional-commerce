package com.commerce.duhan.domain.product

import com.commerce.duhan.domain.common.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "product")
@Entity
class Product(
    val brandId: Long,
    val name: String,
    val description: String,
    @Enumerated(EnumType.STRING)
    var status: ProductStatus,
    var likeCount: Long = 0L,
) : BaseEntity() {
    fun onSale() {
        requireNotDeleted()
        status = ProductStatus.ON_SALE
    }

    fun stopSale() {
        requireNotDeleted()
        status = ProductStatus.STOP_SALE
    }

    override fun delete() {
        status = ProductStatus.DELETED
        super.delete()
    }

    fun addLike() {
        requireNotDeleted()
        likeCount++
    }

    fun removeLike() {
        requireNotDeleted()
        likeCount = maxOf(0, likeCount - 1)
    }

    private fun requireNotDeleted() {
        require(status != ProductStatus.DELETED) { "이미 삭제된 상품입니다." }
    }
}
