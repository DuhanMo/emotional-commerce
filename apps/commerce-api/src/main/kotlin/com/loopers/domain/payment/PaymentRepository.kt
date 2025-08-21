package com.loopers.domain.payment

interface PaymentRepository {
    fun save(payment: Payment): Payment

    fun findAllByStatus(status: TransactionStatus): List<Payment>

    fun getByTransactionKey(transactionKey: String): Payment
}
