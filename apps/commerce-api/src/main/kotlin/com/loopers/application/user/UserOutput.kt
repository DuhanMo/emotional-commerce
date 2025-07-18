package com.loopers.application.user

import com.loopers.domain.user.Email
import com.loopers.domain.user.Gender
import com.loopers.domain.user.User
import com.loopers.domain.user.LoginId

data class UserOutput(
    val id: Long,
    val loginId: LoginId,
    val email: Email,
    val birthDate: String,
    val gender: Gender,
) {
    companion object {
        fun from(user: User): UserOutput = UserOutput(
            id = user.id,
            loginId = user.loginId,
            email = user.email,
            birthDate = user.birthDate,
            gender = user.gender,
        )
    }
}
