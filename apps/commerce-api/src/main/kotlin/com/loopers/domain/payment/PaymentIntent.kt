package com.loopers.domain.payment

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "payment_intent")
@Entity
class PaymentIntent(
    val orderId: Long,
    val idempotentKey: String,
    @Enumerated(EnumType.STRING)
    val paymentMethod: PaymentMethod,
    val amount: Money,
): BaseEntity()
