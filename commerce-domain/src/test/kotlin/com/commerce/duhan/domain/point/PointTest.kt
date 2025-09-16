package com.commerce.duhan.domain.point

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PointTest : StringSpec({
    "음수를 충전하면 예외가 발생한다" {
        val point = Point(userId = 1L)
        shouldThrow<IllegalArgumentException> {
            point.charge(-200)
        }
    }

    "포인트가 부족한 경우 사용하면 예외가 발생한다" {
        val point = Point(userId = 1L, amount = 100)
        shouldThrow<IllegalArgumentException> {
            point.use(101)
        }
    }

    "포인트를 충전하면 보유 포인트가 증가한다" {
        val point = Point(userId = 1L, amount = 100)

        point.charge(1)

        point.amount shouldBe 101
    }

    "포인트를 사용하면 보유 포인트가 감소한다" {
        val point = Point(userId = 1L, amount = 100)

        point.use(50)

        point.amount shouldBe 50
    }
})
