package com.loopers.application.order

import com.loopers.domain.order.Order
import com.loopers.domain.order.OrderStatus

data class PlaceOrderOutput(
    val id: Long,
    val userId: Long,
    val status: OrderStatus,
    val totalAmount: Long,
    val orderLines: List<OrderLineOutput>,
) {
    data class AddressOutput(
        val street: String,
        val city: String,
        val zipCode: String,
        val detailAddress: String?,
    )

    data class OrderLineOutput(
        val productId: Long,
        val quantity: Int,
        val unitPrice: Long,
    )

    companion object {
        fun from(info: Order): PlaceOrderOutput = PlaceOrderOutput(
            id = info.id,
            userId = info.userId,
            status = info.status,
            totalAmount = info.totalAmount,
            orderLines = info.orderLines.map { orderLine ->
                OrderLineOutput(
                    productId = orderLine.productId,
                    quantity = orderLine.quantity,
                    unitPrice = orderLine.unitPrice,
                )
            },
        )
    }
}
