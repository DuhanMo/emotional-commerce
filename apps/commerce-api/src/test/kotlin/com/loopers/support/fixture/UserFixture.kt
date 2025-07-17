package com.loopers.support.fixture

import com.loopers.domain.user.Email
import com.loopers.domain.user.Gender
import com.loopers.domain.user.User
import com.loopers.domain.user.UserId

private val TEST_USER_ID = UserId("test123")
private val TEST_USER_EMAIL = Email("test@test.com")
private const val TEST_USER_BIRTH_DATE = "2020-01-01"

fun createUser(
    userId: UserId = TEST_USER_ID,
    email: Email = TEST_USER_EMAIL,
    birthDate: String = TEST_USER_BIRTH_DATE,
    gender: Gender = Gender.MALE,
): User = User(
    userId = userId,
    email = email,
    birthDate = birthDate,
    gender = gender,
)
