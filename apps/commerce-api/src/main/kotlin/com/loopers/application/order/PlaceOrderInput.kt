package com.loopers.application.order

import com.loopers.domain.order.CreateOrderCommand
import com.loopers.domain.order.OrderInfo.OrderLineInfo
import com.loopers.domain.user.LoginId
import com.loopers.domain.user.User

data class PlaceOrderInput(
    val loginId: LoginId,
    val orderItems: List<OrderLineInput>,
) {
    fun toCreateOrderCommand(
        user: User,
    ): CreateOrderCommand = CreateOrderCommand(
        userId = user.id,
        orderLines = orderItems.map {
            OrderLineInfo(
                productId = it.productId,
                quantity = it.quantity,
                unitPrice = it.unitPrice,
            )
        },
    )

    data class OrderLineInput(
        val productId: Long,
        val quantity: Int,
        val unitPrice: Long,
    )
}
