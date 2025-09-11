package com.loopers.application.payment

import com.loopers.domain.payment.PaymentMethod
import org.springframework.stereotype.Service

@Service
class PaymentRequestService(
    processors: List<PaymentProcessor>,
) {
    private val processors: Map<PaymentMethod, PaymentProcessor> = processors.associateBy { it.support() }

    fun requestPayment(command: RequestPaymentCommand) {
        val processor = processors[command.paymentMethod]
            ?: throw IllegalArgumentException("Unsupported payment method ${command.paymentMethod}")
        processor.process(command)
    }
}
