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
    val orderNumber: String,
    val idempotentKey: String,
    val transactionKey: String?,
    @Enumerated(EnumType.STRING)
    val method: PaymentMethod,
    val amount: Money,
    @Enumerated(EnumType.STRING)
    var status: TransactionStatus,
    var reason: String? = null,
) : BaseEntity() {
    fun success() {
        this.status = TransactionStatus.SUCCESS
    }

    fun fail(reason: String?) {
        this.status = TransactionStatus.FAILED
        reason?.let { this.reason = it }
    }
}
