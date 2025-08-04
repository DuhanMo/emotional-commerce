package com.loopers.domain.order

import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
) {
    fun createOrder(command: CreateOrderCommand): Order {
        val order = command.toOrder()
        return orderRepository.save(order)
    }
}
