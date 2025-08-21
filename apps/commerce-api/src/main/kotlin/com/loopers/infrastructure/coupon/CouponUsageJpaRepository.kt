package com.loopers.infrastructure.coupon

import com.loopers.domain.coupon.CouponUsage
import org.springframework.data.jpa.repository.JpaRepository

interface CouponUsageJpaRepository : JpaRepository<CouponUsage, Long> {
    fun findByOrderId(orderId: Long): CouponUsage?
}
