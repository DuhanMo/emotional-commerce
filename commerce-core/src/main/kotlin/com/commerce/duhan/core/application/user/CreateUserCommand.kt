package com.commerce.duhan.core.application.user

import com.commerce.duhan.core.domain.user.BirthDate
import com.commerce.duhan.core.domain.user.Email
import com.commerce.duhan.core.domain.user.Gender
import com.commerce.duhan.core.domain.user.LoginId
import com.commerce.duhan.core.domain.user.User

data class CreateUserCommand(
    val loginId: LoginId,
    val email: Email,
    val birthDate: BirthDate,
    val gender: Gender,
)

data class CreateUserResult(
    val id: Long,
    val loginId: LoginId,
    val email: Email,
    val birthDate: BirthDate,
    val gender: Gender,
) {
    companion object {
        fun from(user: User): CreateUserResult = CreateUserResult(
            id = user.id,
            loginId = user.loginId,
            email = user.email,
            birthDate = user.birthDate,
            gender = user.gender,
        )
    }
}
