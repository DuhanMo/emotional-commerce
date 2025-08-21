package com.loopers.domain.payment

import com.loopers.domain.support.Money

data class RequestPaymentCommand(
    val userId: Long,
    val paymentMethod: PaymentMethod,
    val orderId: Long,
    val orderNumber: String,
    val cardType: CardType?,
    val cardNumber: String?,
    val amount: Money,
)
