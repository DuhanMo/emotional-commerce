package com.loopers.domain.user

data class UserRegisterCommand(
    val userId: UserId,
    val email: Email,
    val birthDate: String,
    val gender: Gender,
) {
    fun toUser(): User = User(
        userId = userId,
        email = email,
        birthDate = birthDate,
        gender = gender,
    )
}
