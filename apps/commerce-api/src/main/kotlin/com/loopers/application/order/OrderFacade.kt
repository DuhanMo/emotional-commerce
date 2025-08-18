package com.loopers.application.order

import com.loopers.domain.coupon.CouponQueryService
import com.loopers.domain.coupon.IssuedCouponService
import com.loopers.domain.order.Address
import com.loopers.domain.order.OrderService
import com.loopers.domain.payment.Money
import com.loopers.domain.payment.PayRequestCommand
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.payment.PaymentService
import com.loopers.domain.product.ProductService
import com.loopers.domain.user.LoginId
import com.loopers.domain.user.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderFacade(
    private val userQueryService: UserQueryService,
    private val couponQueryService: CouponQueryService,
    private val orderService: OrderService,
    private val paymentService: PaymentService,
) {
    // 주문생성(주문하기, 상품 -> 주문하기, 카트 -> 주문하기)
    @Transactional
    fun placeOrder(input: PlaceOrderInput): PlaceOrderOutput {
        val user = userQueryService.getByLoginId(input.loginId)
        val createOrderCommand = input.toCreateOrderCommand(user)
        val order = orderService.createOrder(createOrderCommand)
        return PlaceOrderOutput.from(order)
    }

    // 결제 요청(쿠폰, 주소, .. 결제하기누르면)
    fun requestPayment(input: RequestPaymentInput) {
        val user = userQueryService.getByLoginId(input.loginId)
        // 주문 유효성 검증 (쿠폰, 결제금액 등)
        input.issuedCouponId?.let { couponQueryService.findByIssuedCouponId(it) }
        paymentService.payRequest(input.toPayRequestCommand(user.id))
        // 재고 차감, 쿠폰 사용
        orderService.captureOrderInfo(input.toCaptureOrderInfoCommand())
    }

    // 웹훅
    fun commitPayment() {
        // 결제 완료 후 주문 상태 변경
        // Order -> PAID
        // 결제실패일시 재고 차감, 쿠폰 사용 등 리커버
    }
}







