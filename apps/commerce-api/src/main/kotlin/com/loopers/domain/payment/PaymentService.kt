package com.loopers.domain.payment

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService(
    payProcessors: List<PayProcessor>,
) {
    private val payProcessors by lazy { payProcessors.associateBy { it.support() } }

    @Transactional
    fun payRequest(command: PayRequestCommand): PayResult {
        val payProcessor = payProcessors[command.paymentMethod]
            ?: throw IllegalArgumentException("지원하지 않는 결제 방법입니다: ${PaymentMethod.POINT}")

        payProcessor.process(PayProcessCommand(command.userId, command.orderId, command.amount))
        return PayResult.SUCCESS
    }
}
