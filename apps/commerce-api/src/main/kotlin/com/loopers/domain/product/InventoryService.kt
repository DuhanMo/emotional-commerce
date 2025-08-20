package com.loopers.domain.product

import com.loopers.domain.order.Order
import com.loopers.domain.product.InventoryReservation.InventoryReservationStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InventoryService(
    private val inventoryRepository: InventoryRepository,
    private val inventoryReservationRepository: InventoryReservationRepository,
) {
    @Transactional
    fun reserveAll(order: Order) {
        val orderLines = order.orderLines
        val skuIds = orderLines.map { it.skuId }
        val inventories = inventoryRepository.findAllBySkuIds(skuIds).associateBy { it.skuId }

        orderLines.forEach {
            val inventory = inventories[it.skuId]
            requireNotNull(inventory)
            inventory.reserve(it.quantity)
        }

        val inventoryReservations = orderLines.map {
            InventoryReservation(
                orderId = order.id,
                skuId = it.skuId,
                quantity = it.quantity,
                status = InventoryReservationStatus.RESERVED,
            )
        }

        inventoryRepository.saveAll(inventories.values.toList())
        inventoryReservationRepository.saveAll(inventoryReservations)
    }
}
