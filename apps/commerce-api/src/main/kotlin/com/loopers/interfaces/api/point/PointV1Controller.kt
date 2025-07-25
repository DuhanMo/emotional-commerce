package com.loopers.interfaces.api.point

import com.loopers.application.point.PointService
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
class PointV1Controller(
    private val pointService: PointService,
) : PointV1ApiSpec {
    @PostMapping("/charge")
    override fun charge(
        @Valid @RequestBody request: PointChargeRequest,
        @RequestHeader("X-USER-ID") loginId: String,
    ): ApiResponse<PointResponse> {
        val response = PointResponse.from(pointService.charge(request.toCommand(loginId)))
        return ApiResponse.success(response)
    }

    @GetMapping
    override fun find(
        @RequestHeader("X-USER-ID") loginId: String,
    ): ApiResponse<PointResponse> {
        val response = PointResponse.from(pointService.find(LoginId(loginId)))
        return ApiResponse.success(response)
    }
}
