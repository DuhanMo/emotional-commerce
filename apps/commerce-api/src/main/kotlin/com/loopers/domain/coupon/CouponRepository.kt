package com.loopers.domain.coupon

interface CouponRepository {
    fun save(coupon: Coupon): Coupon
    fun getByIdWithLock(id: Long): Coupon
    fun getById(id: Long): Coupon
}
