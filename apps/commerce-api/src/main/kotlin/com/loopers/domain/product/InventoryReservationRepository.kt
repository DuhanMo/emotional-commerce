package com.loopers.domain.product

interface InventoryReservationRepository {
    fun saveAll(inventoryReservations: List<InventoryReservation>): List<InventoryReservation>

    fun findAllByOrderId(orderId: Long): List<InventoryReservation>
}
