package com.loopers.application.order

import com.loopers.domain.common.events.DomainEventPublisher
import com.loopers.domain.common.events.OrderCreatedEvent
import com.loopers.domain.coupon.Coupon
import com.loopers.domain.coupon.CouponQueryService
import com.loopers.domain.order.OrderService
import com.loopers.domain.user.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderFacade(
    private val userQueryService: UserQueryService,
    private val couponQueryService: CouponQueryService,
    private val orderService: OrderService,
    private val eventPublisher: DomainEventPublisher,
) {
    @Transactional
    fun placeOrder(input: PlaceOrderInput): PlaceOrderOutput {
        val user = userQueryService.getByLoginId(input.loginId)
        val coupon = findCouponIfUsingCoupon(input.issuedCouponId)
        // TODO 쿠폰 등 검증 실행
        val order = orderService.createOrder(input.toCreateOrderCommand(user.id))
        eventPublisher.publish(OrderCreatedEvent.from(order))
        return PlaceOrderOutput.from(order)
    }

    private fun findCouponIfUsingCoupon(issuedCouponId: Long?): Coupon? =
        issuedCouponId?.let { couponQueryService.findByIssuedCouponId(it) }
}
