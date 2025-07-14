package com.loopers.domain.user

data class UserCreateCommand(
    val uid: String,
    val email: String,
    val birthDate: String,
) {
    fun toUser(): User = User(
        uid = uid,
        email = email,
        birthDate = birthDate,
    )
}
