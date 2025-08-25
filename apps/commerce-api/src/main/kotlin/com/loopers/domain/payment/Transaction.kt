package com.loopers.domain.payment

data class Transaction(
    val transactionKey: String,
    val status: TransactionStatus,
    val reason: String?,
)
