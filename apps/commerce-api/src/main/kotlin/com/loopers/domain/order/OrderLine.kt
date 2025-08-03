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
    val unitPrice: Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    var order: Order? = null,
) : BaseEntity() {
    val lineAmount: Long
        get() = quantity * unitPrice

    init {
        require(quantity > 0) { "수량은 0보다 커야 합니다." }
        require(unitPrice >= 0) { "단가는 0 이상이어야 합니다." }
    }

    fun setOrder(order: Order): OrderLine {
        this.order = order
        return this
    }
}
