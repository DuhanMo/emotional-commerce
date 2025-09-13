package com.commerce.duhan.core.application.user

import com.commerce.duhan.core.domain.user.BirthDate
import com.commerce.duhan.core.domain.user.Email
import com.commerce.duhan.core.domain.user.Gender
import com.commerce.duhan.core.domain.user.LoginId

data class CreateUserCommand(
    val loginId: LoginId,
    val email: Email,
    val birthDate: BirthDate,
    val gender: Gender,
)
