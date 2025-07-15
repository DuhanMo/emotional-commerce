package com.loopers.interfaces.api.user

import com.loopers.interfaces.api.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid

@Tag(name = "User V1 API", description = "유저 관리 API")
interface UserV1ApiSpec {

    @Operation(
        summary = "회원 가입",
        description = "새로운 유저를 등록합니다.",
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "회원 가입 성공"),
            SwaggerApiResponse(responseCode = "400", description = "잘못된 요청"),
            SwaggerApiResponse(responseCode = "500", description = "서버 내부 오류"),
        ],
    )
    fun register(@Valid request: UserRegisterRequest): ApiResponse<UserRegisterResponse>
}
