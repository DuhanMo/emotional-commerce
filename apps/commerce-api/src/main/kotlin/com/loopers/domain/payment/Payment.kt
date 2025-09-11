package com.loopers.domain.payment

import com.loopers.domain.BaseEntity
import com.loopers.domain.support.Money
import jakarta.persistence.Entity
import jakarta.persistence.Table

/**
 * 결제 정보
 * 주문당 여러 결제 정보가 생성될 수 있지만,
 * 결제 요청은 유니크하며 idempotencyKey로 식별된다.
 */
@Table(name = "payment")
@Entity
class Payment(
    val userId: Long,
    val orderId: Long,
    val idempotencyKey: String,
    val method: PaymentMethod,
    val amount: Money,
    val status: PaymentStatus = PaymentStatus.REQUESTED,
    val transactionId: String? = null,
    val cardType: CardType? = null,
    val cardNumber: String? = null,
    id: Long = 0L,
) : BaseEntity(id) {
    companion object {
        fun requestByPoint(
            userId: Long,
            orderId: Long,
            idempotencyKey: String,
            method: PaymentMethod,
            amount: Money,
        ): Payment = Payment(
            userId = userId,
            orderId = orderId,
            idempotencyKey = idempotencyKey,
            method = method,
            amount = amount,
        )

        fun requestByCard(
            userId: Long,
            orderId: Long,
            idempotencyKey: String,
            method: PaymentMethod,
            amount: Money,
            cardType: CardType,
            cardNumber: String,
        ): Payment = Payment(
            userId = userId,
            orderId = orderId,
            idempotencyKey = idempotencyKey,
            method = method,
            amount = amount,
            cardType = cardType,
            cardNumber = cardNumber,
        )
    }
}
