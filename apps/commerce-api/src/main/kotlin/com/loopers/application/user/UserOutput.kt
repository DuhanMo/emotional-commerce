package com.loopers.application.user

import com.loopers.domain.user.Email
import com.loopers.domain.user.Gender
import com.loopers.domain.user.User
import com.loopers.domain.user.UserId

data class UserOutput(
    val id: Long,
    val userId: UserId,
    val email: Email,
    val birthDate: String,
    val gender: Gender,
) {
    companion object {
        fun from(user: User): UserOutput = UserOutput(
            id = user.id,
            userId = user.userId,
            email = user.email,
            birthDate = user.birthDate,
            gender = user.gender,
        )
    }
}
