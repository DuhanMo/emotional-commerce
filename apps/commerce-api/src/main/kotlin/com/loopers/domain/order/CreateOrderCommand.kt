package com.loopers.domain.order

data class CreateOrderCommand(
    val userId: Long,
    val deliveryAddress: Address,
    val orderItems: List<OrderItem>,
) {
    data class OrderItem(
        val productId: Long,
        val quantity: Int,
        val unitPrice: Int,
    )

    init {
        require(orderItems.isNotEmpty()) { "주문 상품이 최소 1개는 있어야 합니다." }
    }

    fun toOrder(): Order {
        val orderLines = orderItems.map { item ->
            OrderLine(
                productId = item.productId,
                quantity = item.quantity,
                unitPrice = item.unitPrice,
            )
        }

        return Order(
            userId = userId,
            deliveryAddress = deliveryAddress,
            orderLines = orderLines,
        )
    }
}
