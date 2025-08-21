package com.loopers.domain.payment

import java.util.UUID
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    payProcessors: List<PayProcessor>,
) {
    private val payProcessors by lazy { payProcessors.associateBy { it.support() } }

    fun requestPayment(command: RequestPaymentCommand): Payment {
        val payProcessor = payProcessors[command.paymentMethod]
            ?: throw IllegalArgumentException("지원하지 않는 결제 방법입니다: ${PaymentMethod.POINT}")

        val transaction = payProcessor.process(command)

        return paymentRepository.save(
            Payment(
                orderId = command.orderId,
                orderNumber = command.orderNumber,
                idempotentKey = UUID.randomUUID().toString(),
                transactionKey = transaction.transactionKey,
                method = command.paymentMethod,
                amount = command.amount,
                status = transaction.status,
            ),
        )
    }

    fun success(transactionKey: String) {
        val payment = paymentRepository.getByTransactionKey(transactionKey)

        payment.success()

        paymentRepository.save(payment)
    }

    fun fail(transactionKey: String, reason: String?) {
        val payment = paymentRepository.getByTransactionKey(transactionKey)

        payment.fail(reason)

        paymentRepository.save(payment)
    }
}
