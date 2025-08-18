package com.loopers.domain.order

import com.loopers.domain.payment.Money

data class CaptureOrderInfoCommand(
    val orderId: Long,
    val amount: Money,
    val issuedCouponId: Long?,
    val deliveryAddress: Address,
)