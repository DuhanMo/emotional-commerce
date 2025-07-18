package com.loopers.interfaces.api.wallet

import com.loopers.application.user.WalletService
import com.loopers.domain.user.LoginId
import com.loopers.interfaces.api.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/points")
class WalletV1Controller(
    private val walletService: WalletService,
) {
    @PostMapping("/charge")
    fun charge(
        @Valid @RequestBody request: PointChargeRequest,
        @RequestHeader("X-USER-ID") loginId: String,
    ): ApiResponse<WalletResponse> {
        val response = WalletResponse.from(walletService.charge(request.toCommand(loginId)))
        return ApiResponse.success(response)
    }

    @GetMapping
    fun find(
        @RequestHeader("X-USER-ID") loginId: String,
    ): ApiResponse<WalletResponse> {
        val response = WalletResponse.from(walletService.find(LoginId(loginId)))
        return ApiResponse.success(response)
    }
}
