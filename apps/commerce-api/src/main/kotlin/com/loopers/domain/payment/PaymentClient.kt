package com.loopers.domain.payment

interface PaymentClient {
    fun requestPayment(command: RequestPaymentCommand): Transaction
}
