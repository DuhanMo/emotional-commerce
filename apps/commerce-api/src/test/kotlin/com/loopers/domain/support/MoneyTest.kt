package com.loopers.domain.support

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MoneyTest : StringSpec({

    "Money 생성 시 음수 값이면 예외가 발생한다" {
        shouldThrow<IllegalArgumentException> {
            Money(-1)
        }
    }

    "Money 생성 시 0 이상의 값이면 정상 생성된다" {
        val money = Money(1000)
        money.value shouldBe 1000L
    }

    "Money 덧셈 연산이 정상 동작한다" {
        val money1 = Money(1000)
        val money2 = Money(500)
        val result = money1 + money2
        result.value shouldBe 1500L
    }

    "Money 뺄셈 연산이 정상 동작한다" {
        val money1 = Money(1000)
        val money2 = Money(300)
        val result = money1 - money2
        result.value shouldBe 700L
    }

    "Money 뺄셈 시 결과가 음수가 되면 예외가 발생한다" {
        val money1 = Money(300)
        val money2 = Money(1000)
        shouldThrow<IllegalArgumentException> {
            money1 - money2
        }
    }

    "Money 곱셈 연산이 정상 동작한다" {
        val money = Money(1000)
        val result = money * 3
        result.value shouldBe 3000L
    }

    "Money 나눗셈 연산이 정상 동작한다" {
        val money = Money(1000)
        val result = money / 2
        result.value shouldBe 500L
    }

    "Money 비교 연산이 정상 동작한다" {
        val money1 = Money(1000)
        val money2 = Money(500)
        val money3 = Money(1000)

        (money1 > money2) shouldBe true
        (money2 < money1) shouldBe true
        (money1 == money3) shouldBe true
    }

    "Money.ZERO 상수가 정상 동작한다" {
        Money.ZERO.value shouldBe 0L
    }

    "toString이 값을 반환한다" {
        val money = Money(1000)
        money.toString() shouldBe "1000"
    }

    "sumOfMoney 확장함수가 정상 동작한다" {
        val moneyList = listOf(Money(100), Money(200), Money(300))
        val sum = moneyList.sumOfMoney()
        sum.value shouldBe 600L
    }

    "빈 리스트의 sumOfMoney는 ZERO를 반환한다" {
        val emptyList = emptyList<Money>()
        val sum = emptyList.sumOfMoney()
        sum shouldBe Money.ZERO
    }

})