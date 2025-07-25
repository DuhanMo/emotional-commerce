package com.loopers.support.fixture

import com.loopers.domain.user.BirthDate
import com.loopers.domain.user.Email
import com.loopers.domain.user.Gender
import com.loopers.domain.user.User
import com.loopers.domain.user.LoginId

private val TEST_USER_ID = LoginId("test123")
private val TEST_USER_EMAIL = Email("test@test.com")
private val TEST_USER_BIRTH_DATE = BirthDate("2020-01-01")

fun createUser(
    loginId: LoginId = TEST_USER_ID,
    email: Email = TEST_USER_EMAIL,
    birthDate: BirthDate = TEST_USER_BIRTH_DATE,
    gender: Gender = Gender.MALE,
): User = User(
    loginId = loginId,
    email = email,
    birthDate = birthDate,
    gender = gender,
)
