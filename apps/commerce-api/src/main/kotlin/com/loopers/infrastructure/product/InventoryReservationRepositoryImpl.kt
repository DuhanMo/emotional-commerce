package com.loopers.infrastructure.product

import com.loopers.domain.product.InventoryReservation
import com.loopers.domain.product.InventoryReservationRepository
import org.springframework.stereotype.Repository

@Repository
class InventoryReservationRepositoryImpl(
    private val jpaRepository: InventoryReservationJpaRepository,
) : InventoryReservationRepository {

    override fun saveAll(inventoryReservations: List<InventoryReservation>): List<InventoryReservation> =
        jpaRepository.saveAll(inventoryReservations)
}
