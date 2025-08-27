package com.loopers.interfaces.listener

import com.loopers.domain.common.events.OrderCreatedEvent
import com.loopers.domain.coupon.IssuedCouponService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class CouponHandler(
    private val issuedCouponService: IssuedCouponService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    fun handle(event: OrderCreatedEvent) {
        try {
            event.issuedCouponId?.let { issuedCouponService.pendingCoupon(event.userId, it) }
        } catch (e: Exception) {
            logger.error("쿠폰 사용예정 처리 실패.(eventType: ${event.eventType})", e)
            // TODO: 알림 또는 Retry, Recover 추가
        }
    }
}
