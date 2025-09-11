package com.loopers.application.payment

import com.loopers.domain.payment.CardType
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.support.Money

data class RequestPaymentCommand(
    val userId: Long,
    val orderId: Long,
    val idempotencyKey: String,
    val paymentMethod: PaymentMethod,
    val amount: Money,
    val cardType: CardType?,
    val cardNumber: String?,
)
