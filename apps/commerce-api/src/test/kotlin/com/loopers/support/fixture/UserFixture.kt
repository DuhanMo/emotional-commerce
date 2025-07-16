package com.loopers.support.fixture

import com.loopers.domain.user.Gender
import com.loopers.domain.user.User
import com.loopers.domain.user.UserId

private const val TEST_USER_ID = "test123"
private const val TEST_USER_EMAIL = "test@test.com"
private const val TEST_USER_BIRTH_DATE = "2020-01-01"

fun createUser(
    userId: UserId = UserId(TEST_USER_ID),
    email: String = TEST_USER_EMAIL,
    birthDate: String = TEST_USER_BIRTH_DATE,
    gender: Gender = Gender.MALE,
): User = User(
    userId = userId,
    email = email,
    birthDate = birthDate,
    gender = gender,
)