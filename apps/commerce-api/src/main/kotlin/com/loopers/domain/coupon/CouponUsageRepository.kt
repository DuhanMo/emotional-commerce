package com.loopers.domain.coupon

interface CouponUsageRepository {
    fun getByOrderId(orderId: Long): CouponUsage

    fun save(couponUsage: CouponUsage): CouponUsage
}
