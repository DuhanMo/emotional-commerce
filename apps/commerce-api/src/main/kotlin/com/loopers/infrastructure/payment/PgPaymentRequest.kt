package com.loopers.infrastructure.payment

import com.loopers.domain.payment.CardType
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern

data class PgPaymentRequest(
    val orderId: String,
    val cardType: CardType,
    @field:Pattern(regexp = "^\\d{4}-\\d{4}-\\d{4}-\\d{4}$")
    val cardNo: String,
    @field:Min(1)
    val amount: Long,
    val callbackUrl: String,
)
