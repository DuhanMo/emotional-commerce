package com.loopers.support.fixture

import com.loopers.domain.coupon.Coupon
import com.loopers.domain.coupon.CouponPolicy

fun createCoupon(
    name: String = "테스트 쿠폰",
    maxIssueCount: Long = 10L,
    policy: CouponPolicy = CouponPolicy.FIXED_AMOUNT,
    discountValue: Long = 2_000L,
    issuedCount: Long = 0L,
    id: Long = 0L,
): Coupon = Coupon(
    name = name,
    maxIssueCount = maxIssueCount,
    policy = policy,
    discountValue = discountValue,
    issuedCount = issuedCount,
    id = id,
)
