package com.loopers.application.payment

import com.loopers.domain.payment.PaymentMethod

interface PaymentProcessor {
    fun support(): PaymentMethod
    fun process(command: RequestPaymentCommand)
}
