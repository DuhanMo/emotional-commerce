package com.loopers.support.fixture

import com.loopers.domain.payment.CardType
import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.payment.PaymentStatus
import com.loopers.domain.support.Money

const val TEST_IDEMPOTENCY_KEY: String = "idempotency-key-1234"

fun createPayment(
    userId: Long = 1L,
    orderId: Long = 1L,
    idempotencyKey: String = TEST_IDEMPOTENCY_KEY,
    method: PaymentMethod = PaymentMethod.POINT,
    amount: Money = Money(1000),
    status: PaymentStatus = PaymentStatus.REQUESTED,
    transactionId: String? = null,
    cardType: CardType? = null,
    cardNumber: String? = null,
    id: Long = 0L,
): Payment = Payment(
    userId = userId,
    orderId = orderId,
    idempotencyKey = idempotencyKey,
    method = method,
    amount = amount,
    status = status,
    transactionId = transactionId,
    cardType = cardType,
    cardNumber = cardNumber,
    id = id,
)
