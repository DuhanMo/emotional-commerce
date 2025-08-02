package com.loopers.application.order

import com.loopers.domain.order.Address
import com.loopers.domain.order.CreateOrderCommand
import com.loopers.domain.order.OrderInfo.OrderLineInfo
import com.loopers.domain.order.PayMethod
import com.loopers.domain.user.LoginId

data class PlaceOrderInput(
    val loginId: LoginId,
    val address: AddressInput,
    val payMethod: PayMethod,
    val orderItems: List<OrderLineInput>,
) {
    fun toCommand(userId: Long): CreateOrderCommand = CreateOrderCommand(
        userId = userId,
        deliveryAddress = Address(
            street = address.street,
            city = address.city,
            zipCode = address.zipCode,
            detailAddress = address.detailAddress,
        ),
        payMethod = payMethod,
        orderLines = this.orderItems.map { item ->
            OrderLineInfo(
                productId = item.productId,
                quantity = item.quantity,
                unitPrice = item.unitPrice,
            )
        },
    )

    data class AddressInput(
        val street: String,
        val city: String,
        val zipCode: String,
        val detailAddress: String?,
    )

    data class OrderLineInput(
        val productId: Long,
        val quantity: Int,
        val unitPrice: Long,
    )
}
