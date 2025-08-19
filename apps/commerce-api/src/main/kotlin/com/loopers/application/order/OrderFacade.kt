package com.loopers.application.order

import com.loopers.domain.coupon.CouponQueryService
import com.loopers.domain.order.OrderService
import com.loopers.domain.payment.PaymentService
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
    fun placeOrder(input: PlaceOrderInput) {
    }
}
