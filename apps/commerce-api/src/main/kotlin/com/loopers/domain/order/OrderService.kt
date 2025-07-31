package com.loopers.domain.order

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderService(
    private val orderRepository: OrderRepository,
) {
    @Transactional
    fun createOrder(command: CreateOrderCommand): Order {
        val order = command.toOrder()
        return orderRepository.save(order)
    }
}