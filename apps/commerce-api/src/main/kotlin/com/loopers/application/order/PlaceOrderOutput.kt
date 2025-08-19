package com.loopers.application.order

import com.loopers.domain.order.Order
import com.loopers.domain.support.Money

data class PlaceOrderOutput(
    val id: Long,
    val userId: Long,
    val status: Order.OrderStatus,
    val totalAmount: Money,
    val orderLines: List<OrderLineOutput>,
) {
    data class OrderLineOutput(
        val productId: Long,
        val quantity: Long,
        val unitPrice: Money,
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
