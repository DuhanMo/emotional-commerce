package com.loopers.interfaces.api.user

import com.loopers.domain.user.Email
import com.loopers.domain.user.Gender
import com.loopers.domain.user.LoginId
import com.loopers.domain.user.UserRegisterCommand
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Schema(description = "회원 가입 요청")
data class UserRegisterRequest(
    @Schema(description = "로그인 ID", example = "test123", required = true)
    @field:NotBlank(message = "로그인 ID 는 빈 값일 수 없습니다.")
    val loginId: String,
    @Schema(description = "이메일 주소", example = "test@test.com", required = true)
    @field:jakarta.validation.constraints.Email(message = "유효한 이메일 형식이어야 합니다.")
    val email: String,
    @Schema(
        description = "생년월일 (YYYY-MM-DD 형식)",
        example = "2002-04-28",
        required = true,
        pattern = "\\d{4}-\\d{2}-\\d{2}",
    )
    @field:NotBlank(message = "생년월일은 빈 값일 수 없습니다.")
    val birthDate: String,
    @Schema(description = "성별", example = "MALE", required = true, allowableValues = ["MALE", "FEMALE"])
    @field:NotNull(message = "성별은 빈 값일 수 없습니다.")
    val gender: Gender?,
) {
    fun toCommand(): UserRegisterCommand = UserRegisterCommand(
        loginId = LoginId(loginId),
        email = Email(email),
        birthDate = birthDate,
        gender = gender!!,
    )
}
