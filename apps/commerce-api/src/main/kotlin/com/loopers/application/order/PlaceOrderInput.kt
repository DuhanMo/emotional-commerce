package com.loopers.application.order

import com.loopers.domain.order.PayMethod
import com.loopers.domain.user.LoginId

data class PlaceOrderInput(
    val loginId: LoginId,
    val address: AddressInput,
    val payMethod: PayMethod,
    val orderItems: List<OrderLineInput>,
    val issuedCouponId: Long?,
) {
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
