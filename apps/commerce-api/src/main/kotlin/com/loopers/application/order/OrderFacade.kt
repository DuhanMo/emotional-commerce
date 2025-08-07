package com.loopers.application.order

import com.loopers.domain.coupon.CouponQueryService
import com.loopers.domain.coupon.IssuedCouponService
import com.loopers.domain.order.OrderService
import com.loopers.domain.payment.PaymentService
import com.loopers.domain.product.ProductService
import com.loopers.domain.user.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderFacade(
    private val userQueryService: UserQueryService,
    private val couponQueryService: CouponQueryService,
    private val orderService: OrderService,
    private val paymentService: PaymentService,
    private val productService: ProductService,
    private val issuedCouponService: IssuedCouponService,
) {
    @Transactional
    fun placeOrder(input: PlaceOrderInput): PlaceOrderOutput {
        val user = userQueryService.getByLoginId(input.loginId)
        val coupon = couponQueryService.findByIssuedCouponId(input.issuedCouponId)
        val order = orderService.createOrder(input.toCommand(user.id, coupon))
        paymentService.pay(user, order)
        input.issuedCouponId?.let { issuedCouponService.useCoupon(user.id, it) }
        productService.deductStock(order)
        return PlaceOrderOutput.from(order)
    }
}
