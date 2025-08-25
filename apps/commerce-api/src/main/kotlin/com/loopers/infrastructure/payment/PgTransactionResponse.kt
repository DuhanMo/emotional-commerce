package com.loopers.infrastructure.payment

import com.loopers.domain.payment.Transaction
import com.loopers.domain.payment.TransactionStatus

data class PgTransactionResponse(
    val transactionKey: String,
    val status: PgTransactionStatus,
    val reason: String?,
) {
    fun toDomain(): Transaction = Transaction(
        transactionKey,
        TransactionStatus.valueOf(status.name),
        reason,
    )
}
