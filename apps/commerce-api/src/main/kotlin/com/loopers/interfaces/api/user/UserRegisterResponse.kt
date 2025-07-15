package com.loopers.interfaces.api.user

import com.loopers.application.user.UserOutput
import com.loopers.domain.user.Gender
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "회원 가입 응답")
data class UserRegisterResponse(
    @Schema(description = "생성된 사용자의 고유 ID", example = "1")
    val id: Long,
    @Schema(description = "사용자 고유 ID", example = "testuser123")
    val userId: String,
    @Schema(description = "사용자 이메일 주소", example = "test@example.com")
    val email: String,
    @Schema(description = "생년월일", example = "1995-03-15")
    val birthDate: String,
    @Schema(description = "성별", example = "MALE")
    val gender: Gender,
) {
    companion object {
        fun from(output: UserOutput): UserRegisterResponse = UserRegisterResponse(
            id = output.id,
            userId = output.userId.value,
            email = output.email,
            birthDate = output.birthDate,
            gender = output.gender,
        )
    }
}
