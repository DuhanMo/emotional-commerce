package com.loopers.domain.order

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository,
) {
    @Transactional
    fun createOrder(command: CreateOrderCommand): OrderInfo {
        val order = command.toOrder()
        return OrderInfo.from(orderRepository.save(order))
    }
}
