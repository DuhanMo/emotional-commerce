package com.commerce.duhan.api.user

import com.commerce.duhan.api.support.ApiResponse
import com.commerce.duhan.application.user.UserApplicationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserV1Controller(
    private val service: UserApplicationService,
) {
    @PostMapping
    fun create(
        @RequestBody request: CreateUserRequest,
    ): ApiResponse<UserResponse> {
        val response = UserResponse.from(service.create(request.toCommand()))
        return ApiResponse.success(response)
    }

    @GetMapping("/me")
    fun getMe(
        @RequestHeader("X-USER-ID") userId: Long,
    ): ApiResponse<GetMeResponse> {
        val response = GetMeResponse.from(service.getMe(userId))
        return ApiResponse.success(response)
    }
}
