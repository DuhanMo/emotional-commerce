package com.loopers.domain.order

data class OrderInfo(
    val id: Long,
    val userId: Long,
    val deliveryAddress: Address,
    val payMethod: PayMethod,
    val status: OrderStatus,
    val totalAmount: Long,
    val orderLineInfos: List<OrderLineInfo>,
) {
    data class OrderLineInfo(
        val productId: Long,
        val quantity: Int,
        val unitPrice: Long,
    ) {
        val lineAmount: Long get() = quantity * unitPrice
    }

    companion object {
        fun from(order: Order): OrderInfo {
            return OrderInfo(
                id = order.id,
                userId = order.userId,
                deliveryAddress = order.deliveryAddress,
                payMethod = order.payMethod,
                status = order.status,
                totalAmount = order.totalAmount,
                orderLineInfos = order.orderLines.map { orderLine ->
                    OrderLineInfo(
                        productId = orderLine.productId,
                        quantity = orderLine.quantity,
                        unitPrice = orderLine.unitPrice,
                    )
                },
            )
        }
    }
}
