// package com.loopers.support.fixture
//
// import com.loopers.domain.order.Address
// import com.loopers.domain.order.CreateOrderCommand
// import com.loopers.domain.order.Order
// import com.loopers.domain.order.OrderInfo.OrderLineInfo
// import com.loopers.domain.order.OrderLine
//
// private val TEST_ADDRESS = Address(
//    street = "강남대로 123",
//    city = "서울시",
//    zipCode = "12345",
//    detailAddress = "테스트 상세주소",
// )
//
// fun createOrderCommand(
//    userId: Long = 1L,
//    orderLines: List<OrderLineInfo> = listOf(
//        OrderLineInfo(productId = 1L, quantity = 2, unitPrice = 10000),
//        OrderLineInfo(productId = 2L, quantity = 1, unitPrice = 5000),
//    ),
// ): CreateOrderCommand = CreateOrderCommand(
//    userId = userId,
//    orderLines = orderLines,
// )
//
// fun createOrder(
//    userId: Long = 1L,
//    deliveryAddress: Address = TEST_ADDRESS,
//    status: OrderStatus = OrderStatus.PENDING,
// ) = Order(
//    userId = userId,
//    deliveryAddress = deliveryAddress,
//    status = status,
// )
//
// fun createOrderLine(
//    productId: Long = 1L,
//    quantity: Int = 1,
//    unitPrice: Long = 10_000L,
// ): OrderLine {
//    return OrderLine(
//        productId = productId,
//        quantity = quantity,
//        unitPrice = unitPrice,
//    )
// }
