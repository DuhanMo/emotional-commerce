package com.loopers.application.order

import com.loopers.domain.order.OrderInfo
import com.loopers.domain.order.OrderStatus
import com.loopers.domain.order.PayMethod

data class PlaceOrderOutput(
    val id: Long,
    val userId: Long,
    val deliveryAddress: AddressOutput,
    val payMethod: PayMethod,
    val status: OrderStatus,
    val totalAmount: Long,
    val orderLineInfos: List<OrderLineOutput>,
) {
    data class AddressOutput(
        val street: String,
        val city: String,
        val zipCode: String,
        val detailAddress: String?,
    )

    data class OrderLineOutput(
        val productId: Long,
        val quantity: Int,
        val unitPrice: Long,
    )

    companion object {
        fun from(info: OrderInfo): PlaceOrderOutput = PlaceOrderOutput(
            id = info.id,
            userId = info.userId,
            deliveryAddress = AddressOutput(
                street = info.deliveryAddress.street,
                city = info.deliveryAddress.city,
                zipCode = info.deliveryAddress.zipCode,
                detailAddress = info.deliveryAddress.detailAddress,
            ),
            payMethod = info.payMethod,
            status = info.status,
            totalAmount = info.totalAmount,
            orderLineInfos = info.orderLineInfos.map { orderLine ->
                OrderLineOutput(
                    productId = orderLine.productId,
                    quantity = orderLine.quantity,
                    unitPrice = orderLine.unitPrice,
                )
            },
        )
    }
}
