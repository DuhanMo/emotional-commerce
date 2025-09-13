package com.commerce.duhan.api.user

import com.commerce.duhan.core.application.user.CreateUserCommand
import com.commerce.duhan.core.application.user.CreateUserResult
import com.commerce.duhan.core.domain.user.BirthDate
import com.commerce.duhan.core.domain.user.Email
import com.commerce.duhan.core.domain.user.Gender
import com.commerce.duhan.core.domain.user.LoginId

data class CreateUserRequest(
    val loginId: String,
    val email: String,
    val birthDate: String,
    val gender: Gender,
) {
    fun toCommand(): CreateUserCommand = CreateUserCommand(
        loginId = LoginId(loginId),
        email = Email(email),
        birthDate = BirthDate(birthDate),
        gender = gender,
    )
}

data class CreateUserResponse(
    val id: Long,
    val loginId: String,
    val email: String,
    val birthDate: String,
    val gender: Gender,
) {
    companion object {
        fun from(result: CreateUserResult): CreateUserResponse = CreateUserResponse(
            id = result.id,
            loginId = result.loginId.value,
            email = result.email.value,
            birthDate = result.birthDate.value,
            gender = result.gender,
        )
    }
}
