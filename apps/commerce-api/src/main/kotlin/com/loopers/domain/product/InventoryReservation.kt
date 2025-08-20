package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.Instant

@Table(name = "inventory_reservation")
@Entity
class InventoryReservation(
    val orderId: Long,
    val skuId: Long,
    val quantity: Long,
    @Enumerated(EnumType.STRING)
    val status: InventoryReservationStatus,
    val expiresAt: Instant = Instant.now().plusSeconds(3600),
) : BaseEntity() {

    enum class InventoryReservationStatus {
        RESERVED,
        COMMITTED,
        RELEASED,
    }
}
