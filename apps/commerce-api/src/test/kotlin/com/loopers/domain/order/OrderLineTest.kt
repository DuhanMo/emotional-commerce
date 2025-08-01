package com.loopers.domain.order

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class OrderLineTest : StringSpec({

    "상품 ID가 양수가 아닌 경우, 주문 상품 객체 생성에 실패한다" {
        listOf(0L, -1L, -100L).forEach { invalidProductId ->
            shouldThrow<IllegalArgumentException> {
                OrderLine(
                    productId = invalidProductId,
                    quantity = 1,
                    unitPrice = 10000,
                )
            }
        }
    }

    "수량이 0 이하인 경우, 주문 상품 객체 생성에 실패한다" {
        listOf(0, -1, -10).forEach { invalidQuantity ->
            shouldThrow<IllegalArgumentException> {
                OrderLine(
                    productId = 1L,
                    quantity = invalidQuantity,
                    unitPrice = 10000,
                )
            }
        }
    }

    "단가가 음수인 경우, 주문 상품 객체 생성에 실패한다" {
        listOf(-1L, -100L, -10000L).forEach { invalidUnitPrice ->
            shouldThrow<IllegalArgumentException> {
                OrderLine(
                    productId = 1L,
                    quantity = 1,
                    unitPrice = invalidUnitPrice,
                )
            }
        }
    }

    "단가가 0인 경우, 주문 상품 객체 생성에 성공한다" {
        shouldNotThrow<IllegalArgumentException> {
            OrderLine(
                productId = 1L,
                quantity = 1,
                unitPrice = 0,
            )
        }
    }

    "올바른 값으로 주문 상품 객체를 생성할 수 있다" {
        shouldNotThrow<IllegalArgumentException> {
            OrderLine(
                productId = 1L,
                quantity = 2,
                unitPrice = 10000,
            )
        }
    }

    "라인 금액은 수량 × 단가로 계산된다" {
        val orderLine = OrderLine(
            productId = 1L,
            quantity = 3,
            unitPrice = 5000,
        )

        orderLine.lineAmount shouldBe 15000
    }

    "수량이 1이고 단가가 10000인 경우 라인 금액은 10000이다" {
        val orderLine = OrderLine(
            productId = 1L,
            quantity = 1,
            unitPrice = 10000,
        )

        orderLine.lineAmount shouldBe 10000
    }

    "단가가 0인 경우 라인 금액은 0이다" {
        val orderLine = OrderLine(
            productId = 1L,
            quantity = 5,
            unitPrice = 0,
        )

        orderLine.lineAmount shouldBe 0
    }
})
