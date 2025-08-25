package com.loopers.infrastructure.batch

import com.loopers.domain.payment.PaymentRepository
import com.loopers.domain.payment.TransactionStatus.INTERNAL_ERROR
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class InternalErrorPaymentProcessBatchJob(
    private val service: InternalErrorPaymentProcessBatchJobService,
    private val paymentRepository: PaymentRepository,
) {
    /**
     * 10분마다 장애발생한 결제에 대해 PG 상태 조회 후
     * 결제 / 주문 상태 갱신.
     */
    @Scheduled(fixedDelay = 300000)
    fun processInternalErrorPayment() {
        val payments = paymentRepository.findAllByStatus(INTERNAL_ERROR)
        payments.chunked(100) { chunkedPayments ->
            chunkedPayments.forEach {
                service.process()
            }
        }
    }
}
