package com.loopers.domain.product

import com.loopers.domain.order.OrderInfo
import com.loopers.domain.product.InventoryReservation.InventoryReservationStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InventoryService(
    private val inventoryRepository: InventoryRepository,
    private val inventoryReservationRepository: InventoryReservationRepository,
) {
    @Transactional
    fun reserveAll(orderId: Long, orderLines: List<OrderInfo.OrderLineInfo>) {
        val skuIds = orderLines.map { it.skuId }
        val inventories = inventoryRepository.findAllBySkuIds(skuIds).associateBy { it.skuId }

        orderLines.forEach {
            val inventory = inventories[it.skuId]
            requireNotNull(inventory)
            inventory.reserve(it.quantity)
        }

        val inventoryReservations = orderLines.map {
            InventoryReservation(
                orderId = orderId,
                skuId = it.skuId,
                quantity = it.quantity,
                status = InventoryReservationStatus.RESERVED,
            )
        }

        inventoryRepository.saveAll(inventories.values.toList())
        inventoryReservationRepository.saveAll(inventoryReservations)
    }

    @Transactional
    fun commitAll(orderId: Long) {
        val (reservations, inventories) = findReservationsAndInventories(orderId)

        reservations.forEach { reservation ->
            val inventory = inventories[reservation.skuId]
            requireNotNull(inventory)

            reservation.commit()
            inventory.commit(reservation.quantity)
        }

        inventoryReservationRepository.saveAll(reservations)
        inventoryRepository.saveAll(inventories.values.toList())
    }

    @Transactional
    fun releaseAll(orderId: Long) {
        val (reservations, inventories) = findReservationsAndInventories(orderId)

        reservations.forEach { reservation ->
            val inventory = inventories[reservation.skuId]
            requireNotNull(inventory)

            reservation.release()
            inventory.release(reservation.quantity)
        }

        inventoryReservationRepository.saveAll(reservations)
        inventoryRepository.saveAll(inventories.values.toList())
    }

    private fun findReservationsAndInventories(orderId: Long): Pair<List<InventoryReservation>, Map<Long, Inventory>> {
        val reservations = inventoryReservationRepository.findAllByOrderId(orderId)
        val skuIds = reservations.map { it.skuId }
        val inventories = inventoryRepository.findAllBySkuIds(skuIds).associateBy { it.skuId }
        return Pair(reservations, inventories)
    }
}
