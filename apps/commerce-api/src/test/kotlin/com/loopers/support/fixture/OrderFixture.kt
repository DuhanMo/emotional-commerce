package com.loopers.support.fixture

import com.loopers.domain.order.Address
import com.loopers.domain.order.CreateOrderCommand
import com.loopers.domain.order.OrderInfo.OrderLineInfo
import com.loopers.domain.order.PayMethod

private val TEST_ADDRESS = Address(
    street = "강남대로 123",
    city = "서울시",
    zipCode = "12345",
    detailAddress = "테스트 상세주소",
)

fun createOrderCommand(
    userId: Long = 1L,
    deliveryAddress: Address = TEST_ADDRESS,
    payMethod: PayMethod = PayMethod.POINT,
    orderLines: List<OrderLineInfo> = listOf(
        OrderLineInfo(productId = 1L, quantity = 2, unitPrice = 10000),
        OrderLineInfo(productId = 2L, quantity = 1, unitPrice = 5000),
    ),
): CreateOrderCommand = CreateOrderCommand(
    userId = userId,
    deliveryAddress = deliveryAddress,
    payMethod = payMethod,
    orderLines = orderLines,
)
