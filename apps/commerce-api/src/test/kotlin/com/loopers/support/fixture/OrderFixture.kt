package com.loopers.support.fixture

import com.loopers.domain.order.Address
import com.loopers.domain.order.Order
import com.loopers.domain.order.Order.OrderStatus
import com.loopers.domain.support.Money

val TEST_ADDRESS = Address(
    street = "강남대로 123",
    city = "서울시",
    zipCode = "12345",
    detailAddress = "테스트 상세주소",
)
private const val TEST_ORDER_NUMBER = "20250820183522a1b2c3"

fun createOrder(
    userId: Long = 1L,
    status: OrderStatus = OrderStatus.PENDING,
    deliveryAddress: Address = TEST_ADDRESS,
    totalAmount: Money = Money(10_000L),
) = Order(
    userId = userId,
    orderNumber = TEST_ORDER_NUMBER,
    status = status,
    deliveryAddress = deliveryAddress,
    totalAmount = totalAmount,
)
