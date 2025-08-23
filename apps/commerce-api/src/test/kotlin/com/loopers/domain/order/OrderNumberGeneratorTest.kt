package com.loopers.domain.order

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.matchers.string.shouldMatch

class OrderNumberGeneratorTest : StringSpec({

    "주문번호 생성 시 20자리 문자열이 반환된다" {
        val orderNumber = OrderNumberGenerator.generate()
        orderNumber shouldHaveLength 20
    }

    "주문번호는 yyyyMMddHHmmss + 6자리 랜덤 형식이다" {
        val orderNumber = OrderNumberGenerator.generate()
        // 14자리 timestamp + 6자리 random = 20자리
        orderNumber shouldMatch Regex("""\d{14}[0-9a-f]{6}""")
    }

    "연속으로 생성한 주문번호들은 서로 다르다" {
        val orderNumber1 = OrderNumberGenerator.generate()
        val orderNumber2 = OrderNumberGenerator.generate()
        val orderNumber3 = OrderNumberGenerator.generate()

        orderNumber1 shouldNotBe orderNumber2
        orderNumber2 shouldNotBe orderNumber3
        orderNumber1 shouldNotBe orderNumber3
    }

    "생성된 주문번호는 현재 연도를 포함한다" {
        val orderNumber = OrderNumberGenerator.generate()
        val currentYear = java.time.LocalDateTime.now().year.toString()
        orderNumber.substring(0, 4) shouldBe currentYear
    }

    "여러 번 생성해도 모두 20자리 길이를 유지한다" {
        repeat(10) {
            val orderNumber = OrderNumberGenerator.generate()
            orderNumber shouldHaveLength 20
        }
    }
})
