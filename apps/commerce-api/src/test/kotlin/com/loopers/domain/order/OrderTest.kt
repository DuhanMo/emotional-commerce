package com.loopers.domain.order

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class OrderTest : StringSpec({
    "주문 상품이 비어있는 경우, 주문 상품을 더하면 실패한다" {
        val address = Address("강남대로", "서울시", "12345", "상세주소")
        val emptyOrderLines = emptyList<OrderLine>()

        shouldThrow<IllegalArgumentException> {
            val order = Order(
                userId = 1L,
                payMethod = PayMethod.POINT,
                deliveryAddress = address,
            )
            order.addOrderLines(emptyOrderLines)
        }
    }

    "올바른 값으로 주문 객체를 생성할 수 있다" {
        val address = Address("강남대로", "서울시", "12345", "상세주소")

        shouldNotThrow<IllegalArgumentException> {
            Order(
                userId = 1L,
                payMethod = PayMethod.POINT,
                deliveryAddress = address,
            )
        }
    }

    "새로 생성된 주문의 초기 상태는 PENDING이다" {
        val address = Address("강남대로", "서울시", "12345", "상세주소")

        val order = Order(
            userId = 1L,
            payMethod = PayMethod.POINT,
            deliveryAddress = address,
        )

        order.status shouldBe OrderStatus.PENDING
        order.isPending() shouldBe true
        order.isPaid() shouldBe false
        order.isPaymentFailed() shouldBe false
    }

    "주문의 총 금액은 모든 주문 상품의 금액 합계이다" {
        val address = Address("강남대로", "서울시", "12345", "상세주소")
        val orderLines = listOf(
            // 20000원
            OrderLine(productId = 1L, quantity = 2, unitPrice = 10000),
            // 5000원
            OrderLine(productId = 2L, quantity = 1, unitPrice = 5000),
            // 9000원
            OrderLine(productId = 3L, quantity = 3, unitPrice = 3000),
        )

        val order = Order(
            userId = 1L,
            payMethod = PayMethod.POINT,
            deliveryAddress = address,
        )
        order.addOrderLines(orderLines)

        order.totalAmount shouldBe 34000
    }

    "PENDING 상태에서 결제 완료로 상태를 변경할 수 있다" {
        val address = Address("강남대로", "서울시", "12345", "상세주소")
        val order = Order(
            userId = 1L,
            payMethod = PayMethod.POINT,
            deliveryAddress = address,
        )

        order.paid()

        order.status shouldBe OrderStatus.PAID
        order.isPaid() shouldBe true
        order.isPending() shouldBe false
        order.isPaymentFailed() shouldBe false
    }

    "PENDING 상태에서 결제 실패로 상태를 변경할 수 있다" {
        val address = Address("강남대로", "서울시", "12345", "상세주소")
        val order = Order(
            userId = 1L,
            payMethod = PayMethod.POINT,
            deliveryAddress = address,
        )

        order.paymentFailed()

        order.status shouldBe OrderStatus.PAYMENT_FAILED
        order.isPaymentFailed() shouldBe true
        order.isPending() shouldBe false
        order.isPaid() shouldBe false
    }

    "PAID 상태에서는 결제 완료로 상태 변경을 시도할 수 없다" {
        val address = Address("강남대로", "서울시", "12345", "상세주소")

        val order = Order(
            userId = 1L,
            payMethod = PayMethod.POINT,
            deliveryAddress = address,
        )

        order.paid()

        shouldThrow<IllegalArgumentException> {
            order.paid()
        }
    }

    "PAID 상태에서는 결제 실패로 상태 변경을 시도할 수 없다" {
        val address = Address("강남대로", "서울시", "12345", "상세주소")

        val order = Order(
            userId = 1L,
            payMethod = PayMethod.POINT,
            deliveryAddress = address,
        )

        order.paid()

        shouldThrow<IllegalArgumentException> {
            order.paymentFailed()
        }
    }

    "PAYMENT_FAILED 상태에서는 결제 완료로 상태 변경을 시도할 수 없다" {
        val address = Address("강남대로", "서울시", "12345", "상세주소")

        val order = Order(
            userId = 1L,
            payMethod = PayMethod.POINT,
            deliveryAddress = address,
        )

        order.paymentFailed()

        shouldThrow<IllegalArgumentException> {
            order.paid()
        }
    }

    "PAYMENT_FAILED 상태에서는 결제 실패로 상태 변경을 시도할 수 없다" {
        val address = Address("강남대로", "서울시", "12345", "상세주소")
        val order = Order(
            userId = 1L,
            payMethod = PayMethod.POINT,
            deliveryAddress = address,
        )

        order.paymentFailed()

        shouldThrow<IllegalArgumentException> {
            order.paymentFailed()
        }
    }
})
