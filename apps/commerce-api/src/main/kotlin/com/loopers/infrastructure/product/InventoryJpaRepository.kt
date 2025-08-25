package com.loopers.infrastructure.product

import com.loopers.domain.product.Inventory
import org.springframework.data.jpa.repository.JpaRepository

interface InventoryJpaRepository : JpaRepository<Inventory, Long> {
    fun findAllBySkuIdIn(skuIds: List<Long>): List<Inventory>
}
