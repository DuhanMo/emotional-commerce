package com.loopers.application.order

import com.loopers.domain.coupon.Coupon
import com.loopers.domain.coupon.CouponQueryService
import com.loopers.domain.coupon.IssuedCouponService
import com.loopers.domain.order.OrderService
import com.loopers.domain.product.InventoryService
import com.loopers.domain.user.User
import com.loopers.domain.user.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderFacade(
    private val userQueryService: UserQueryService,
    private val couponQueryService: CouponQueryService,
    private val orderService: OrderService,
    private val inventoryService: InventoryService,
    private val issuedCouponService: IssuedCouponService,
) {
    @Transactional
    fun placeOrder(input: PlaceOrderInput) {
        val user = userQueryService.getByLoginId(input.loginId)
        val coupon = findCouponIfUsingCoupon(input.issuedCouponId)
        val order = orderService.createOrder(input.toCreateOrderCommand(user.id, coupon))
        inventoryService.reserveAll(order)
        useCouponIfUsingCoupon(input.issuedCouponId, user)
    }

    private fun findCouponIfUsingCoupon(issuedCouponId: Long?): Coupon? =
        issuedCouponId?.let { couponQueryService.findByIssuedCouponId(it) }

    private fun useCouponIfUsingCoupon(issuedCouponId: Long?, user: User) {
        issuedCouponId?.let { issuedCouponService.pendingCoupon(user.id, it) }
    }
}
