package com.loopers.domain.payment

import com.loopers.domain.support.Money

data class PayRequestCommand(
    val userId: Long,
    val orderId: Long,
    val paymentMethod: PaymentMethod,
    val amount: Money,
)

data class PayProcessCommand(
    val userId: Long,
    val orderId: Long,
    val amount: Money,
)
