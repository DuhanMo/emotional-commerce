package com.loopers.domain.order

interface OrderRepository {
    fun save(order: Order): Order

    fun getById(orderId: Long): Order

    fun findByUserId(userId: Long): List<Order>

    fun getByOrderNumber(orderNumber: String): Order
}
