package com.loopers.domain.point

import com.loopers.domain.user.LoginId
import com.loopers.support.fixture.createUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

class PointTest : StringSpec({
    "0 이하의 정수로 포인트를 충전하는 경우, 예외 발생한다" {
        listOf(0, -1, -100).forAll {
            shouldThrow<IllegalArgumentException> {
                Point(createUser()).charge(0)
            }
        }
    }

    "0 초과의 정수로 포인트를 충전하는 경우, 포인트가 증가한다" {
        val point = Point(createUser())

        point.charge(100)

        point.amount shouldBe 100
    }

    "기존 포인트가 존재하는 경우, 포인트를 충전하면 포인트가 합산된다" {
        val point = Point(user = createUser(), amount = 500)

        point.charge(100)

        point.amount shouldBe 600
    }

    "포인트는 유저를 참조한다" {
        val user = createUser(loginId = LoginId("gildong123"))

        val point = Point(user)

        point.user.loginId shouldBe LoginId("gildong123")
    }
})
