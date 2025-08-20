package com.loopers.domain.product

interface InventoryRepository {
    fun findAllBySkuIds(skuIds: List<Long>): List<Inventory>

    fun saveAll(inventories: List<Inventory>): List<Inventory>
}
