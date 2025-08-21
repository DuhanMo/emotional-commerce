package com.loopers.infrastructure.payment

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "pg-api", url = "\${pg.api.url}")
interface PgPaymentApi {
    @PostMapping("/api/v1/payments", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun requestPayment(
        @RequestHeader("X-USER-ID") pgUserId: String,
        @RequestBody request: PgPaymentRequest,
    ): PgApiResponse<PgTransactionResponse>

    @GetMapping("/api/v1/payments/{transactionKey}")
    fun getPayment(
        @RequestHeader("X-USER-ID") userId: String,
        @PathVariable transactionKey: String,
    ): PgApiResponse<PgTransactionResponse>

    @GetMapping("/api/v1/payments")
    fun getPaymentByOrderId(
        @RequestHeader("X-USER-ID") userId: String,
        @RequestParam orderId: String,
    ): PgApiResponse<PgTransactionResponse>
}
