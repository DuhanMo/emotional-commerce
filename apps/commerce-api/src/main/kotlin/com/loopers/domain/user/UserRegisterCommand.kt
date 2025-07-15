package com.loopers.domain.user

data class UserRegisterCommand(
    val uid: String,
    val email: String,
    val birthDate: String,
    val gender: Gender,
) {
    fun toUser(): User = User(
        uid = uid,
        email = email,
        birthDate = birthDate,
        gender = gender,
    )
}
