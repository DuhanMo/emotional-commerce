package com.loopers.interfaces.api.point

import com.loopers.interfaces.api.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@Tag(name = "Point V1 API", description = "포인트 관리 API")
interface PointV1ApiSpec {

    @Operation(
        summary = "포인트 충전",
        description = "포인트를 충전합니다.",
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "포인트 충전 성공"),
            SwaggerApiResponse(responseCode = "400", description = "잘못된 요청"),
            SwaggerApiResponse(responseCode = "500", description = "서버 내부 오류"),
        ],
    )
    fun charge(
        @Schema(name = "포인트 충전 요청")
        @Valid
        @RequestBody request: PointChargeRequest,
        @Parameter(
            name = "X-USER-ID",
            description = "조회할 유저의 ID",
            `in` = ParameterIn.HEADER,
            required = true,
            schema = Schema(type = "string"),
        )
        @Schema(name = "로그인 ID", description = "조회할 유저의 ID")
        @RequestHeader("X-USER-ID") loginId: String,
        ): ApiResponse<PointResponse>

    @Operation(
        summary = "내 포인트 조회",
        description = "내 포인트를 조회합니다.",
    )
    @ApiResponses(
        value = [
            SwaggerApiResponse(responseCode = "200", description = "포인트 조회 성공"),
            SwaggerApiResponse(responseCode = "400", description = "잘못된 요청"),
            SwaggerApiResponse(responseCode = "500", description = "서버 내부 오류"),
        ],
    )
    fun find(
        @Parameter(
            name = "X-USER-ID",
            description = "조회할 유저의 ID",
            `in` = ParameterIn.HEADER,
            required = true,
            schema = Schema(type = "string"),
        )
        @Schema(name = "로그인 ID", description = "조회할 유저의 ID")
        @RequestHeader("X-USER-ID") loginId: String,
    ): ApiResponse<PointResponse>
}
