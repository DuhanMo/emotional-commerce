package com.loopers.domain.user

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll

class UserTest : StringSpec(
    {
        "ID가 영문 및 숫자 10자 이내 형식이 아닌 경우, User 객체 생성에 실패한다" {
            listOf(
                "한글ID",
                "aaa",
                "111",
                "12345678901",
            ).forAll { uid ->
                shouldThrow<IllegalArgumentException> {
                    User(uid = uid, email = "test@test.com", birthDate = "2000-01-01")
                }
            }
        }

        "ID가 영문 및 숫자 10자 이내 형식인 경우, User 객체 생성에 성공한다" {
            listOf(
                "a123456789",
                "123456789b",
                "12345abc",
            ).forAll { uid ->
                shouldNotThrow<IllegalArgumentException> {
                    User(uid = uid, email = "test@test.com", birthDate = "2000-01-01")
                }
            }
        }

        "이메일 형식이 올바르지 않은 경우, User 객체 생성에 실패한다" {
            listOf(
                "invalidemail",
                "invalid@.com",
                "@invalid.com",
                "invalid@domain",
            ).forAll { email ->
                shouldThrow<IllegalArgumentException> {
                    User(uid = "test123", email = email, birthDate = "2000-01-01")
                }
            }
        }

        "이메일 형식이 올바른 경우, User 객체 생성에 성공한다" {
            listOf(
                "test@test.com",
                "gildong@gmail.com",
                "xx@yy.zz",
            ).forAll { email ->
                shouldNotThrow<IllegalArgumentException> {
                    User(uid = "test123", email = email, birthDate = "2000-01-01")
                }
            }
        }

        "생년월일이 yyyy-MM-dd 형식이 아닌 경우, User 객체 생성에 실패한다" {
            listOf(
                "2025/01/01",
                "25-01-01",
                "2025-1-1",
                "2025-13-01",
                "2025-01-32",
                "01-01-2025",
                "2025년 1월 1일",
                "invalid-date",
                "2025-02-31",
            ).forAll { birthDate ->
                shouldThrow<IllegalArgumentException> {
                    User(uid = "test123", email = "test@test.com", birthDate = birthDate)
                }
            }
        }

        "생년월일이 yyyy-MM-dd 형식인 경우, User 객체 생성에 성공한다" {
            listOf(
                "2025-01-01",
                "2000-12-31",
                "2023-11-15",
                "2024-02-29",
            ).forAll { birthDate ->
                shouldNotThrow<IllegalArgumentException> {
                    User(uid = "test123", email = "test@test.com", birthDate = birthDate)
                }
            }
        }
    },
)
