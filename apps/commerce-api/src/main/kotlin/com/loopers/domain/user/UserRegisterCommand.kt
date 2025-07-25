package com.loopers.domain.user

data class UserRegisterCommand(
    val loginId: LoginId,
    val email: Email,
    val birthDate: BirthDate,
    val gender: Gender,
) {
    fun toUser(): User = User(
        loginId = loginId,
        email = email,
        birthDate = birthDate,
        gender = gender,
    )
}
