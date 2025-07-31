package com.loopers.support.fixture

import com.loopers.domain.order.Address
import com.loopers.domain.order.CreateOrderCommand

private val TEST_ADDRESS = Address(
    street = "강남대로 123",
    city = "서울시",
    zipCode = "12345",
    detailAddress = "테스트 상세주소"
)

fun createOrderCommand(
    userId: Long = 1L,
    deliveryAddress: Address = TEST_ADDRESS,
    orderItems: List<CreateOrderCommand.OrderItem> = listOf(
        CreateOrderCommand.OrderItem(productId = 1L, quantity = 2, unitPrice = 10000),
        CreateOrderCommand.OrderItem(productId = 2L, quantity = 1, unitPrice = 5000)
    )
): CreateOrderCommand = CreateOrderCommand(
    userId = userId,
    deliveryAddress = deliveryAddress,
    orderItems = orderItems
)

fun createAddress(
    street: String = "테스트 도로명",
    city: String = "테스트 도시",
    zipCode: String = "12345",
    detailAddress: String? = "테스트 상세주소"
): Address = Address(
    street = street,
    city = city,
    zipCode = zipCode,
    detailAddress = detailAddress
)