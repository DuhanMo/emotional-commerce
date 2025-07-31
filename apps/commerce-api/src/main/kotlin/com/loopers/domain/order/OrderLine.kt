package com.loopers.domain.order

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "order_line")
@Entity
class OrderLine(
    val productId: Long,
    val quantity: Int,
    val unitPrice: Int,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    val order: Order? = null,
) : BaseEntity() {
    val lineAmount: Int
        get() = quantity * unitPrice

    init {
        require(productId > 0) { "상품 ID는 양수여야 합니다." }
        require(quantity > 0) { "수량은 0보다 커야 합니다." }
        require(unitPrice >= 0) { "단가는 0 이상이어야 합니다." }
    }

    override fun guard() {
        require(productId > 0) { "상품 ID는 양수여야 합니다." }
        require(quantity > 0) { "수량은 0보다 커야 합니다." }
        require(unitPrice >= 0) { "단가는 0 이상이어야 합니다." }
    }
}
