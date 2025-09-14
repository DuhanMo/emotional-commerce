package com.commerce.duhan.api.user

import com.commerce.duhan.core.application.user.CreateUserCommand
import com.commerce.duhan.core.application.user.GetMeResult
import com.commerce.duhan.core.application.user.UserResult
import com.commerce.duhan.core.domain.common.Money
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

data class UserResponse(
    val id: Long,
    val loginId: String,
    val email: String,
    val birthDate: String,
    val gender: Gender,
) {
    companion object {
        fun from(result: UserResult): UserResponse = UserResponse(
            id = result.id,
            loginId = result.loginId.value,
            email = result.email.value,
            birthDate = result.birthDate.value,
            gender = result.gender,
        )
    }
}

data class GetMeResponse(
    val user: UserResponse,
    val point: PointResponse,
) {
    companion object {
        fun from(result: GetMeResult): GetMeResponse = GetMeResponse(
            user = UserResponse.from(result.user),
            point = PointResponse(
                id = result.point.id,
                amount = result.point.amount,
            ),
        )
    }
}

data class PointResponse(
    val id: Long,
    val amount: Money,
)
