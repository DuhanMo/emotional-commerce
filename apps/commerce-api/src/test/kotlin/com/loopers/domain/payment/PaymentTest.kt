package com.loopers.domain.payment

import com.loopers.domain.support.Money
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PaymentTest : StringSpec({

    "결제 성공 처리 시 상태가 SUCCESS로 변경된다" {
        val payment = Payment(
            orderId = 1L,
            orderNumber = "ORDER-001",
            idempotentKey = "IDEMPOTENT-001",
            transactionKey = "TRANSACTION-001",
            method = PaymentMethod.CARD,
            amount = Money(10000),
            status = TransactionStatus.PENDING
        )

        payment.success()

        payment.status shouldBe TransactionStatus.SUCCESS
    }

    "결제 실패 처리 시 상태가 FAILED로 변경되고 실패 사유가 설정된다" {
        val payment = Payment(
            orderId = 1L,
            orderNumber = "ORDER-001", 
            idempotentKey = "IDEMPOTENT-001",
            transactionKey = "TRANSACTION-001",
            method = PaymentMethod.POINT,
            amount = Money(5000),
            status = TransactionStatus.PENDING
        )

        val failureReason = "카드 한도 초과"
        payment.fail(failureReason)

        payment.status shouldBe TransactionStatus.FAILED
        payment.reason shouldBe failureReason
    }

    "결제 실패 처리 시 사유가 null이면 reason이 설정되지 않는다" {
        val payment = Payment(
            orderId = 1L,
            orderNumber = "ORDER-001",
            idempotentKey = "IDEMPOTENT-001", 
            transactionKey = "TRANSACTION-001",
            method = PaymentMethod.CARD,
            amount = Money(15000),
            status = TransactionStatus.PENDING,
            reason = null
        )

        payment.fail(null)

        payment.status shouldBe TransactionStatus.FAILED
        payment.reason shouldBe null
    }

    "Payment 객체 생성 시 초기값이 올바르게 설정된다" {
        val payment = Payment(
            orderId = 100L,
            orderNumber = "ORDER-100",
            idempotentKey = "IDEMPOTENT-100",
            transactionKey = "TRANSACTION-100", 
            method = PaymentMethod.POINT,
            amount = Money(25000),
            status = TransactionStatus.PENDING
        )

        payment.orderId shouldBe 100L
        payment.orderNumber shouldBe "ORDER-100"
        payment.idempotentKey shouldBe "IDEMPOTENT-100"
        payment.transactionKey shouldBe "TRANSACTION-100"
        payment.method shouldBe PaymentMethod.POINT
        payment.amount shouldBe Money(25000)
        payment.status shouldBe TransactionStatus.PENDING
    }

})