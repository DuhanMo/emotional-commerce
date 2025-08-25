package com.loopers.infrastructure.product

import com.loopers.domain.product.InventoryReservation
import org.springframework.data.jpa.repository.JpaRepository

interface InventoryReservationJpaRepository : JpaRepository<InventoryReservation, Long> {
    fun findAllByOrderId(orderId: Long): List<InventoryReservation>
}
