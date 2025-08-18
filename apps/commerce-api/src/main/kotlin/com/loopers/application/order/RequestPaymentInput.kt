package com.loopers.application.order

import com.loopers.domain.order.CaptureOrderInfoCommand
import com.loopers.domain.payment.PayRequestCommand

data class RequestPaymentInput(
    val loginId: LoginId,
    val orderId: Long,
    val paymentMethod: PaymentMethod,
    val cardType: CardType?,
    val cardNo: String,
    val amount: Money,
    val issuedCouponId: Long?,
    val deliveryAddress: Address,
) {
    fun toPayRequestCommand(userId: Long): PayRequestCommand = PayRequestCommand(
        userId = userId,
        orderId = orderId,
        paymentMethod = paymentMethod,
        amount = amount,
    )

    fun toCaptureOrderInfoCommand(): CaptureOrderInfoCommand = CaptureOrderInfoCommand(
        orderId = orderId,
        amount = amount,
        issuedCouponId = issuedCouponId,
        deliveryAddress = deliveryAddress,
    )
}