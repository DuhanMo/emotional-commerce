package com.loopers.interfaces.api.user

import com.loopers.application.user.UserFacade
import com.loopers.application.user.UserRegisterInput
import com.loopers.domain.user.BirthDate
import com.loopers.domain.user.Email
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
@RequestMapping("/api/v1/users")
class UserV1Controller(
    private val userFacade: UserFacade,
) : UserV1ApiSpec {
    @PostMapping
    override fun register(
        @Valid @RequestBody request: UserRegisterRequest,
    ): ApiResponse<UserResponse> {
        val response = UserResponse.from(
            userFacade.register(
                UserRegisterInput(
                    loginId = LoginId(request.loginId),
                    email = Email(request.email),
                    birthDate = BirthDate(request.birthDate),
                    gender = request.gender!!,
                ),
            ),
        )
        return ApiResponse.success(response)
    }

    @GetMapping("/me")
    override fun getMe(
        @RequestHeader("X-USER-ID") loginId: String,
    ): ApiResponse<UserResponse> {
        val response = UserResponse.from(userFacade.getMe(LoginId(loginId)))
        return ApiResponse.success(response)
    }
}
