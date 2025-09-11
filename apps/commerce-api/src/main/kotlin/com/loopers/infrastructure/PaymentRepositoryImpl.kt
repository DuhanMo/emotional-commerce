package com.loopers.infrastructure

import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.PaymentRepository
import com.loopers.infrastructure.user.PaymentJpaRepository
import org.springframework.stereotype.Repository

@Repository
class PaymentRepositoryImpl(
    private val jpaRepository: PaymentJpaRepository,
) : PaymentRepository {
    override fun save(payment: Payment): Payment = jpaRepository.save(payment)
}
