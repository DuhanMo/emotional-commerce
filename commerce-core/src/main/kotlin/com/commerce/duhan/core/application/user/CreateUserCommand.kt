package com.commerce.duhan.core.application.user

import com.commerce.duhan.core.domain.common.Money
import com.commerce.duhan.core.domain.point.Point
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

data class UserResult(
    val id: Long,
    val loginId: LoginId,
    val email: Email,
    val birthDate: BirthDate,
    val gender: Gender,
) {
    companion object {
        fun from(user: User): UserResult = UserResult(
            id = user.id,
            loginId = user.loginId,
            email = user.email,
            birthDate = user.birthDate,
            gender = user.gender,
        )
    }
}

data class GetMeResult(
    val user: UserResult,
    val point: PointResult,
) {
    companion object {
        fun of(user: User, point: Point): GetMeResult = GetMeResult(
            user = UserResult.from(user),
            point = PointResult(
                id = point.id,
                amount = point.amount,
            ),
        )
    }
}

data class PointResult(
    val id: Long,
    val amount: Money,
)
