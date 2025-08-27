package com.loopers.domain.order

import com.loopers.domain.support.Money

data class OrderInfo(
    val id: Long,
    val userId: Long,
    val status: Order.OrderStatus,
    val totalAmount: Money,
    val orderLineInfos: List<OrderLineInfo>,
) {
    data class OrderLineInfo(
        val productId: Long,
        val skuId: Long,
        val quantity: Long,
        val unitPrice: Money,
    ) {
        companion object {
            fun from(orderLine: OrderLine) = OrderLineInfo(
                productId = orderLine.productId,
                skuId = orderLine.skuId,
                quantity = orderLine.quantity,
                unitPrice = orderLine.unitPrice,
            )
        }
    }

    companion object {
        fun from(order: Order): OrderInfo {
            return OrderInfo(
                id = order.id,
                userId = order.userId,
                status = order.status,
                totalAmount = order.totalAmount,
                orderLineInfos = order.orderLines.map { orderLine ->
                    OrderLineInfo(
                        productId = orderLine.productId,
                        skuId = orderLine.skuId,
                        quantity = orderLine.quantity,
                        unitPrice = orderLine.unitPrice,
                    )
                },
            )
        }
    }
}
