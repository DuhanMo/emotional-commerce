package com.loopers.application.payment

import com.loopers.domain.coupon.IssuedCouponService
import com.loopers.domain.order.OrderQueryService
import com.loopers.domain.order.OrderService
import com.loopers.domain.payment.PaymentMethod.POINT
import com.loopers.domain.payment.PaymentService
import com.loopers.domain.payment.RequestPaymentCommand
import com.loopers.domain.payment.TransactionStatus
import com.loopers.domain.payment.TransactionStatus.FAILED
import com.loopers.domain.payment.TransactionStatus.SUCCESS
import com.loopers.domain.product.InventoryService
import com.loopers.domain.user.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentFacade(
    private val userQueryService: UserQueryService,
    private val paymentService: PaymentService,
    private val inventoryService: InventoryService,
    private val orderQueryService: OrderQueryService,
    private val orderService: OrderService,
    private val issuedCouponService: IssuedCouponService,
) {
    fun requestPayment(input: RequestPaymentInput): RequestPaymentOutput {
        val user = userQueryService.getByLoginId(input.loginId)
        val order = orderQueryService.getById(input.orderId)
        val payment = paymentService.requestPayment(
            RequestPaymentCommand(
                userId = user.id,
                paymentMethod = input.paymentMethod,
                orderId = input.orderId,
                orderNumber = input.orderNumber,
                cardType = input.cardType,
                cardNumber = input.cardNumber,
                amount = input.amount,
            ),
        )
        if (payment.status == TransactionStatus.INTERNAL_ERROR) {
            orderService.error(order)
        }
        if (input.paymentMethod == POINT && payment.status == SUCCESS) {
            orderService.paid(order)
            inventoryService.commitAll(order.id)
            issuedCouponService.commit(order.id)
        }
        return RequestPaymentOutput(payment.status, payment.reason)
    }

    @Transactional
    fun handleTransactionCallback(input: TransactionCallbackInput) {
        val order = orderQueryService.getByOrderNumber(input.orderNumber)
        when (input.status) {
            SUCCESS -> {
                paymentService.success(input.transactionKey)
                orderService.paid(order)
                inventoryService.commitAll(order.id)
                issuedCouponService.commit(order.id)
            }

            FAILED -> {
                paymentService.fail(input.transactionKey, input.reason)
                orderService.payFail(order)
                inventoryService.releaseAll(order.id)
                issuedCouponService.release(order.id)
            }

            else -> {
                throw IllegalArgumentException("처리할 수 없는 거래상태입니다.(status: ${input.status})")
            }
        }
    }
}
