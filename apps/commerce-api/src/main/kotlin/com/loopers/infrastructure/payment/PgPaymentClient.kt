package com.loopers.infrastructure.payment

import com.loopers.domain.payment.PaymentClient
import com.loopers.domain.payment.RequestPaymentCommand
import com.loopers.domain.payment.Transaction
import com.loopers.domain.payment.TransactionStatus
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PgPaymentClient(
    private val paymentApi: PgPaymentApi,
) : PaymentClient {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Retry(name = "paymentClient", fallbackMethod = "requestPaymentFallback")
    @CircuitBreaker(name = "paymentClient")
    override fun requestPayment(command: RequestPaymentCommand): Transaction {
        logger.info("결제 요청 $command")

        return paymentApi.requestPayment(
            pgUserId = "pg-ec-001",
            request = PgPaymentRequest(
                orderId = command.orderNumber,
                cardType = command.cardType!!,
                cardNo = command.cardNumber!!,
                amount = command.amount.value,
                callbackUrl = "http://localhost:8080/api/v1/payments/callback",
            ),
        ).data!!.toDomain()
    }

    private fun requestPaymentFallback(command: RequestPaymentCommand, ex: Exception): Transaction {
        logger.error("결제 요청 재시도 실패", ex)

        return Transaction(
            transactionKey = "RETRY_NEEDED_${System.currentTimeMillis()}",
            status = TransactionStatus.INTERNAL_ERROR,
            reason = "결제 재시도가 필요합니다: ${ex.message}",
        )
    }
}
