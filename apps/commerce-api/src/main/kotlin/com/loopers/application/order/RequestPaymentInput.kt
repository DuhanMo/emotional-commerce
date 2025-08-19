package com.loopers.application.order

import com.loopers.domain.order.Address
import com.loopers.domain.order.CaptureOrderInfoCommand
import com.loopers.domain.payment.CardType
import com.loopers.domain.payment.PayRequestCommand
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.support.Money
import com.loopers.domain.user.LoginId

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
