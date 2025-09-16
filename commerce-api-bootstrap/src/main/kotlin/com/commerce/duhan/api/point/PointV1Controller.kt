package com.commerce.duhan.api.point

import com.commerce.duhan.api.support.ApiResponse
import com.commerce.duhan.application.point.PointApplicationService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/points")
class PointV1Controller(
    private val service: PointApplicationService,
) {
    @PostMapping("/charge")
    fun charge(
        @Valid @RequestBody request: ChargePointRequest,
        @RequestHeader("X-USER-ID") userId: Long,
    ): ApiResponse<PointResponse> {
        val response = PointResponse.from(service.charge(userId, request.amount!!))
        return ApiResponse.success(response)
    }

    @GetMapping
    fun find(
        @RequestHeader("X-USER-ID") userId: Long,
    ): ApiResponse<PointResponse> {
        val response = PointResponse.from(service.find(userId))
        return ApiResponse.success(response)
    }
}
