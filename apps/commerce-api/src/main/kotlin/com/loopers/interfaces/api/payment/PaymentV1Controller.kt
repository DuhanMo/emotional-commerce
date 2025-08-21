package com.loopers.interfaces.api.payment

import com.loopers.application.payment.PaymentFacade
import com.loopers.domain.user.LoginId
import com.loopers.interfaces.api.ApiResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/payments")
class PaymentV1Controller(
    private val paymentFacade: PaymentFacade,
) {
    @PostMapping
    fun requestPayment(
        @RequestHeader("X-USER-ID") loginId: String,
        @RequestBody request: RequestPaymentRequest,
    ): ApiResponse<RequestPaymentResponse> {
        val response = RequestPaymentResponse.from(paymentFacade.requestPayment(request.toInput(LoginId(loginId))))
        return ApiResponse.success(response)
    }

    @PostMapping("/callback")
    fun callback(
        @RequestBody request: TransactionCallbackRequest,
    ): ApiResponse<Any> {
        paymentFacade.handleTransactionCallback(request.toInput())
        return ApiResponse.success()
    }
}
