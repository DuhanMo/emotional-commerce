package com.loopers.domain.payment

import org.springframework.stereotype.Component

@Component
class CardPayProcessor(
    private val paymentClient: PaymentClient,
) : PayProcessor {
    override fun support(): PaymentMethod = PaymentMethod.CARD

    override fun process(command: RequestPaymentCommand): Transaction {
        // TODO: 카드결제 요청/응답 저장
        return paymentClient.requestPayment(command)
    }
}
