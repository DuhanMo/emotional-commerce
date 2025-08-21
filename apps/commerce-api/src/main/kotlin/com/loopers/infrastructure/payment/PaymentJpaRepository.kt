package com.loopers.infrastructure.payment

import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.TransactionStatus
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentJpaRepository : JpaRepository<Payment, Long> {
    fun findAllByStatus(status: TransactionStatus): List<Payment>

    fun findByTransactionKey(transactionKey: String): Payment?
}
