package com.loopers.support.fixture

import com.loopers.domain.order.Order
import com.loopers.domain.order.Order.OrderStatus
import com.loopers.domain.support.Money

fun createOrder(
    userId: Long = 1L,
    status: OrderStatus = OrderStatus.PENDING,
    totalAmount: Money = Money(10_000L),
) = Order(
    userId = userId,
    status = status,
    totalAmount = totalAmount,
)
