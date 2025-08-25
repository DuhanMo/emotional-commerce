package com.loopers.application.payment

import com.loopers.domain.payment.CardType
import com.loopers.domain.payment.TransactionStatus
import com.loopers.domain.support.Money

data class TransactionCallbackInput(
    val transactionKey: String,
    val orderNumber: String,
    val cardType: CardType,
    val cardNumber: String,
    val amount: Money,
    val status: TransactionStatus,
    val reason: String?,
)
