package com.loopers.interfaces.api.user

import com.loopers.interfaces.api.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse

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
    fun register(@Valid @RequestBody request: UserRegisterRequest): ApiResponse<UserResponse>

    @Operation(
        summary = "내 정보 조회",
        description = "내 정보를 조회합니다.",
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "내 정보 조회 성공"),
            SwaggerApiResponse(responseCode = "400", description = "잘못된 요청"),
            SwaggerApiResponse(responseCode = "500", description = "서버 내부 오류"),
        ],
    )
    fun getMe(
        @Parameter(
            name = "X-USER-ID",
            description = "로그인 아이디",
            `in` = ParameterIn.HEADER,
            required = true,
            schema = Schema(type = "string"),
        )
        @Schema(name = "로그인 아이디", description = "로그인 아이디")
        @RequestHeader("X-USER-ID") loginId: String,
    ): ApiResponse<UserResponse>
}
