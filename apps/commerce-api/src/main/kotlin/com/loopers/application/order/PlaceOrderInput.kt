package com.loopers.application.order

import com.loopers.domain.order.Address
import com.loopers.domain.order.CreateOrderCommand
import com.loopers.domain.order.OrderInfo.OrderLineInfo
import com.loopers.domain.support.Money
import com.loopers.domain.user.LoginId

data class PlaceOrderInput(
    val loginId: LoginId,
    val orderItems: List<OrderLineInput>,
    val totalAmount: Money,
    val deliveryAddress: Address,
    val issuedCouponId: Long?,
) {
    fun toCreateOrderCommand(
        userId: Long,
    ): CreateOrderCommand = CreateOrderCommand(
        userId = userId,
        orderLines = orderItems.map {
            OrderLineInfo(
                productId = it.productId,
                skuId = it.skuId,
                quantity = it.quantity,
                unitPrice = it.unitPrice,
            )
        },
        totalAmount = totalAmount,
        deliveryAddress = deliveryAddress,
        issuedCouponId = issuedCouponId,
    )

    data class OrderLineInput(
        val productId: Long,
        val skuId: Long,
        val quantity: Long,
        val unitPrice: Money,
    )
}
