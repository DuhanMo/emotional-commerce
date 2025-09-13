package com.commerce.duhan.core.fixtures

import com.commerce.duhan.core.domain.user.BirthDate
import com.commerce.duhan.core.domain.user.Email
import com.commerce.duhan.core.domain.user.Gender
import com.commerce.duhan.core.domain.user.LoginId
import com.commerce.duhan.core.domain.user.User

val TEST_LOGIN_ID = LoginId("user123")
val TEST_EMAIL = Email("test@test.com")
val TEST_BIRTH_DATE = BirthDate("1990-01-01")

fun createUser(
    loginId: LoginId = TEST_LOGIN_ID,
    email: Email = TEST_EMAIL,
    birthDate: BirthDate = TEST_BIRTH_DATE,
    gender: Gender = Gender.MALE,
    id: Long = 0L,
): User = User(
    loginId = loginId,
    email = email,
    birthDate = birthDate,
    gender = gender,
    id = id,
)
