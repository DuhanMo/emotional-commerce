package com.loopers.domain.order

import com.loopers.domain.order.Order.OrderStatus
import com.loopers.domain.support.Money
import com.loopers.support.fixture.createOrder
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class OrderTest : StringSpec({
    "주문 상품이 비어있는 경우, 주문 상품을 더하면 실패한다" {
        val order = createOrder(status = OrderStatus.PENDING)
        val emptyOrderLines = emptyList<OrderInfo.OrderLineInfo>()
        shouldThrow<IllegalArgumentException> {
            order.addOrderLines(emptyOrderLines)
        }
    }

    "PENDING 상태에서 결제 완료로 상태를 변경할 수 있다" {
        val order = createOrder(status = OrderStatus.PENDING)

        order.paid()

        order.status shouldBe OrderStatus.PAID
    }

    "PENDING 상태에서 결제 실패로 상태를 변경할 수 있다" {
        val order = createOrder(status = OrderStatus.PENDING)

        order.payFail()

        order.status shouldBe OrderStatus.PAY_FAILED
    }

    "PAID 상태에서는 결제 완료로 상태 변경을 시도할 수 없다" {
        val order = createOrder(userId = 1L, totalAmount = Money(1000), status = OrderStatus.PAID)

        shouldThrow<IllegalArgumentException> {
            order.paid()
        }
    }

    "PAID 상태에서는 결제 실패로 상태 변경을 시도할 수 없다" {
        val order = createOrder(status = OrderStatus.PAID)

        shouldThrow<IllegalArgumentException> {
            order.payFail()
        }
    }

    "PAYMENT_FAILED 상태에서는 결제 완료로 상태 변경을 시도할 수 없다" {
        val order = createOrder(status = OrderStatus.PAY_FAILED)

        shouldThrow<IllegalArgumentException> {
            order.paid()
        }
    }

    "PAY_FAILED 상태에서는 결제 실패로 상태 변경을 시도할 수 없다" {
        val order = createOrder(status = OrderStatus.PAY_FAILED)

        shouldThrow<IllegalArgumentException> {
            order.payFail()
        }
    }
})
