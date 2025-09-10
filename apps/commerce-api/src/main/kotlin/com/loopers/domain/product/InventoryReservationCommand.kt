package com.loopers.domain.product

import com.loopers.domain.order.Order

data class InventoryReservationCommand(
    val orderId: Long,
    val items: List<InventoryItem>,
) {
    data class InventoryItem(
        val skuId: Long,
        val quantity: Long,
    )

    companion object {
        fun from(order: Order): InventoryReservationCommand = InventoryReservationCommand(
            orderId = order.id,
            items = order.orderLines.map {
                InventoryItem(
                    skuId = it.skuId,
                    quantity = it.quantity,
                )
            },
        )
    }
}
