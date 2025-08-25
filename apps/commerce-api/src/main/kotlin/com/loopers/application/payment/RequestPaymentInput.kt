package com.loopers.application.payment

import com.loopers.domain.payment.CardType
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.support.Money
import com.loopers.domain.user.LoginId

data class RequestPaymentInput(
    val loginId: LoginId,
    val orderId: Long,
    val paymentMethod: PaymentMethod,
    val orderNumber: String,
    val cardType: CardType,
    val cardNumber: String,
    val amount: Money,
)
