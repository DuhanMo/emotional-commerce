package com.loopers.interfaces.api.payment

import com.loopers.application.payment.RequestPaymentOutput
import com.loopers.domain.payment.TransactionStatus

data class RequestPaymentResponse(
    val transactionStatus: TransactionStatus,
    val reason: String?,
) {
    companion object {
        fun from(output: RequestPaymentOutput): RequestPaymentResponse = RequestPaymentResponse(
            transactionStatus = output.transactionStatus,
            reason = output.reason,
        )
    }
}
