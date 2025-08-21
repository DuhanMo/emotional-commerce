package com.loopers.infrastructure.payment

import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.PaymentRepository
import com.loopers.domain.payment.TransactionStatus
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Repository

@Repository
class PaymentRepositoryImpl(
    private val jpaRepository: PaymentJpaRepository,
) : PaymentRepository {
    override fun save(payment: Payment): Payment = jpaRepository.save(payment)

    override fun findAllByStatus(status: TransactionStatus): List<Payment> = jpaRepository.findAllByStatus(status)

    override fun getByTransactionKey(transactionKey: String): Payment =
        jpaRepository.findByTransactionKey(transactionKey)
            ?: throw CoreException(ErrorType.NOT_FOUND, "결제를 찾을 수 없습니다.(transactionKey: $transactionKey)")
}
