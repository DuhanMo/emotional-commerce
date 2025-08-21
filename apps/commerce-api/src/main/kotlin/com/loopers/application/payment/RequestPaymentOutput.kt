package com.loopers.application.payment

import com.loopers.domain.payment.TransactionStatus

data class RequestPaymentOutput(
    val transactionStatus: TransactionStatus,
    val reason: String?,
)
