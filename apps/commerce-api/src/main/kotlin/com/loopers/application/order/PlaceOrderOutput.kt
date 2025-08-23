package com.loopers.application.order

import com.loopers.domain.order.Order
import com.loopers.domain.support.Money

data class PlaceOrderOutput(
    val orderId: Long,
    val orderNumber: String,
    val status: Order.OrderStatus,
    val totalAmount: Money,
) {
    companion object {
        fun from(order: Order): PlaceOrderOutput = PlaceOrderOutput(
            orderId = order.id,
            orderNumber = order.orderNumber,
            status = order.status,
            totalAmount = order.totalAmount,
        )
    }
}
