package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "inventory")
@Entity
class Inventory(
    val skuId: Long,
    val availableQty: Long,
    val reservedQty: Long,
    val soldQty: Long,
) : BaseEntity()
