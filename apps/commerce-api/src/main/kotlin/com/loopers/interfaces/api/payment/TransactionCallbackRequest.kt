package com.loopers.interfaces.api.payment

import com.loopers.application.payment.TransactionCallbackInput
import com.loopers.domain.payment.CardType
import com.loopers.domain.payment.TransactionStatus
import com.loopers.domain.support.Money

data class TransactionCallbackRequest(
    val transactionKey: String,
    val orderId: String,
    val cardType: CardType,
    val cardNo: String,
    val amount: Long,
    val status: String,
    val reason: String?,
) {
    fun toInput(): TransactionCallbackInput = TransactionCallbackInput(
        transactionKey = transactionKey,
        orderNumber = orderId,
        cardType = cardType,
        cardNumber = cardNo,
        amount = Money(amount),
        status = TransactionStatus.valueOf(status),
        reason = reason,
    )
}
