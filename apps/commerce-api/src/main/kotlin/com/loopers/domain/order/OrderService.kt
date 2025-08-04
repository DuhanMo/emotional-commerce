package com.loopers.domain.order

import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
) {
    fun createOrder(command: CreateOrderCommand): Order = with(command) {
        val orderLines = orderLines.map { item ->
            OrderLine(
                productId = item.productId,
                quantity = item.quantity,
                unitPrice = item.unitPrice,
            )
        }
        val order = Order(
            userId = userId,
            deliveryAddress = deliveryAddress,
            payMethod = payMethod,
        ).apply {
            addOrderLines(orderLines)
        }
        orderRepository.save(order)
    }
}
