package com.loopers.domain.payment

import com.loopers.domain.BaseEntity
import com.loopers.domain.support.Money
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "payment")
@Entity
class Payment(
    val orderId: Long,
    val idempotentKey: String,
    val transactionId: String,
    @Enumerated(EnumType.STRING)
    val method: PaymentMethod,
    val amount: Money,
    @Enumerated(EnumType.STRING)
    val status: PaymentStatus,
) : BaseEntity() {
    enum class PaymentStatus {
        REQUESTED,
        COMPLETED,
        FAILED,
    }
}
