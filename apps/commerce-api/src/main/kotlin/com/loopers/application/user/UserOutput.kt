package com.loopers.application.user

import com.loopers.domain.user.Gender
import com.loopers.domain.user.User

data class UserOutput(
    val id: Long,
    val uid: String,
    val email: String,
    val birthDate: String,
    val gender: Gender,
) {
    companion object {
        fun from(user: User): UserOutput = UserOutput(
            id = user.id,
            uid = user.uid,
            email = user.email,
            birthDate = user.birthDate,
            gender = user.gender,
        )
    }
}
