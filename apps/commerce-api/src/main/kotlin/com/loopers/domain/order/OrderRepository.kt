package com.loopers.domain.order

interface OrderRepository {
    fun save(order: Order): Order

    fun findById(orderId: Long): Order?

    fun getById(orderId: Long): Order

    fun findByUserId(userId: Long): List<Order>
}