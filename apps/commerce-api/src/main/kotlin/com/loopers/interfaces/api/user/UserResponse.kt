package com.loopers.interfaces.api.user

import com.loopers.application.user.UserOutput
import com.loopers.domain.user.Gender
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "유저 응답")
data class UserResponse(
    @Schema(description = "식별자", example = "1")
    val id: Long,
    @Schema(description = "유저 ID", example = "test123")
    val userId: String,
    @Schema(description = "이메일 주소", example = "test@test.com")
    val email: String,
    @Schema(description = "생년월일", example = "1995-03-15")
    val birthDate: String,
    @Schema(description = "성별", example = "MALE")
    val gender: Gender,
) {
    companion object {
        fun from(output: UserOutput): UserResponse = UserResponse(
            id = output.id,
            userId = output.userId.value,
            email = output.email,
            birthDate = output.birthDate,
            gender = output.gender,
        )
    }
}
