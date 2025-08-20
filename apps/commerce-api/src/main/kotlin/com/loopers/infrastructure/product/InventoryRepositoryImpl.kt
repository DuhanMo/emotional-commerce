package com.loopers.infrastructure.product

import com.loopers.domain.product.Inventory
import com.loopers.domain.product.InventoryRepository
import org.springframework.stereotype.Repository

@Repository
class InventoryRepositoryImpl(
    private val jpaRepository: InventoryJpaRepository,
) : InventoryRepository {
    override fun findAllBySkuIds(skuIds: List<Long>): List<Inventory> = jpaRepository.findAllBySkuIdIn(skuIds)

    override fun saveAll(inventories: List<Inventory>): List<Inventory> = jpaRepository.saveAll(inventories)
}
