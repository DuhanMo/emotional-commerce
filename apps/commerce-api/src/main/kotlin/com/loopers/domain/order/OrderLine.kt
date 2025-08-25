package com.loopers.domain.order

import com.loopers.domain.BaseEntity
import com.loopers.domain.support.Money
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "order_line")
@Entity
class OrderLine(
    val productId: Long,
    val skuId: Long,
    val quantity: Long,
    val unitPrice: Money,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    val order: Order,
) : BaseEntity() {
    val lineAmount: Money
        get() = unitPrice * quantity

    init {
        require(quantity > 0) { "수량은 0보다 커야 합니다." }
    }
}
