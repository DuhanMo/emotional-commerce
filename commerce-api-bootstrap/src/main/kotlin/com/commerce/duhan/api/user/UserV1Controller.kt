package com.commerce.duhan.api.user

import com.commerce.duhan.api.support.ApiResponse
import com.commerce.duhan.core.application.user.UserApplicationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserV1Controller(
    private val service: UserApplicationService,
) {
    @PostMapping
    fun create(@RequestBody request: CreateUserRequest): ApiResponse<CreateUserResponse> {
        val response = CreateUserResponse.from(service.create(request.toCommand()))
        return ApiResponse.success(response)
    }
}
