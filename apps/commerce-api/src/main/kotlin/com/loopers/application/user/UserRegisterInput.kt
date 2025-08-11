package com.loopers.application.user

import com.loopers.domain.user.BirthDate
import com.loopers.domain.user.Email
import com.loopers.domain.user.Gender
import com.loopers.domain.user.LoginId

data class UserRegisterInput(
    val loginId: LoginId,
    val email: Email,
    val birthDate: BirthDate,
    val gender: Gender,
)
