package com.loopers.domain.point

import com.loopers.domain.support.Money
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

class PointTest : StringSpec({
    "0 이하의 정수로 포인트를 충전하는 경우, 예외 발생한다" {
        listOf(0, -1, -100).forAll {
            shouldThrow<IllegalArgumentException> {
                Point(userId = 1L).charge(Money(-100))
            }
        }
    }

    "0 초과의 정수로 포인트를 충전하는 경우, 포인트가 증가한다" {
        val point = Point(userId = 1L)

        point.charge(Money(100))

        point.amount shouldBe Money(100)
    }

    "기존 포인트가 존재하는 경우, 포인트를 충전하면 포인트가 합산된다" {
        val point = Point(userId = 1L, amount = Money(500))

        point.charge(Money(100))

        point.amount shouldBe Money(600)
    }

    "포인트를 사용하는 경우, 포인트가 차감된다" {
        val point = Point(userId = 1L, amount = Money(500))

        point.use(Money(200))

        point.amount shouldBe Money(300)
    }

    "포인트가 부족할 경우, 포인트를 사용하면 예외가 발생한다" {
        val point = Point(userId = 1L, amount = Money(100))

        shouldThrow<IllegalArgumentException> {
            point.use(Money(200))
        }
    }
})
