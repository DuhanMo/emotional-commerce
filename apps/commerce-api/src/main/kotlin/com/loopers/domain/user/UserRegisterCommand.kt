package com.loopers.domain.user

data class UserRegisterCommand(
    val loginId: LoginId,
    val email: Email,
    val birthDate: String,
    val gender: Gender,
) {
    fun toUser(): User = User(
        loginId = loginId,
        email = email,
        birthDate = birthDate,
        gender = gender,
    )
}
