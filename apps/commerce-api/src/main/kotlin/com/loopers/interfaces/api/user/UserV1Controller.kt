package com.loopers.interfaces.api.user

import com.loopers.application.user.UserService
import com.loopers.domain.user.UserId
import com.loopers.interfaces.api.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserV1Controller(
    private val userService: UserService,
) : UserV1ApiSpec {
    @PostMapping
    override fun register(
        @Valid @RequestBody request: UserRegisterRequest,
    ): ApiResponse<UserResponse> {
        val response = UserResponse.from(userService.register(request.toCommand()))
        return ApiResponse.success(response)
    }

    @GetMapping("/me")
    override fun getMe(
        @RequestHeader("X-USER-ID") userId: String,
    ): ApiResponse<UserResponse> {
        val response = UserResponse.from(userService.getMe(UserId(userId)))
        return ApiResponse.success(response)
    }
}
