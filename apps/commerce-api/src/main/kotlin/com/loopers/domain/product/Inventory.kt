package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "inventory")
@Entity
class Inventory(
    val productId: Long,
    val skuId: Long,
    var availableQty: Long,
    var reservedQty: Long,
    var soldQty: Long,
    id: Long = 0L,
) : BaseEntity(id) {
    fun reserve(quantity: Long) {
        require(quantity <= availableQty) { "재고가 부족합니다." }
        availableQty -= quantity
        reservedQty += quantity
    }

    fun commit(quantity: Long) {
        require(quantity <= reservedQty) { "확정재고는 예약재고보다 작아야합니다." }
        reservedQty -= quantity
        soldQty += quantity
    }
}
