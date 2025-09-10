package com.loopers.application.order

import com.loopers.domain.common.events.DomainEventPublisher
import com.loopers.domain.common.events.OrderCreatedEvent
import com.loopers.domain.coupon.Coupon
import com.loopers.domain.coupon.CouponQueryService
import com.loopers.domain.coupon.IssuedCouponService
import com.loopers.domain.order.OrderService
import com.loopers.domain.product.InventoryReservationCommand
import com.loopers.domain.product.InventoryService
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
    fun placeOrder(input: PlaceOrderInput): PlaceOrderOutput {
        val user = userQueryService.getByLoginId(input.loginId)
        val coupon = findCouponIfUsingCoupon(input.issuedCouponId)
        val order = orderService.createOrder(input.toCreateOrderCommand(user.id))
        // todo: 쿠폰 사용 검증

        inventoryService.reserveAll(InventoryReservationCommand.from(order))
        input.issuedCouponId?.let { issuedCouponService.pendingCoupon(user.id, it) }
        return PlaceOrderOutput.from(order)
    }

    private fun findCouponIfUsingCoupon(issuedCouponId: Long?): Coupon? =
        issuedCouponId?.let { couponQueryService.findByIssuedCouponId(it) }
}
