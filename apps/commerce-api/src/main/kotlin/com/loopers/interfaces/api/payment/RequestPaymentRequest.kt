package com.loopers.interfaces.api.payment

import com.loopers.application.payment.RequestPaymentInput
import com.loopers.domain.payment.CardType
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.support.Money
import com.loopers.domain.user.LoginId

data class RequestPaymentRequest(
    val paymentMethod: PaymentMethod,
    val orderId: Long,
    val orderNumber: String,
    val cardType: CardType,
    val cardNumber: String,
    val amount: Money,
) {
    fun toInput(loginId: LoginId): RequestPaymentInput = RequestPaymentInput(
        loginId = loginId,
        paymentMethod = paymentMethod,
        orderId = orderId,
        orderNumber = orderNumber,
        cardType = cardType,
        cardNumber = cardNumber,
        amount = amount,
    )
}
