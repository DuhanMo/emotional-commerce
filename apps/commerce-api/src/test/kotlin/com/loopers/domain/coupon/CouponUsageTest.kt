package com.loopers.domain.coupon

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CouponUsageTest : StringSpec({
    "쿠폰 사용 확정 지으면 상태가 USED로 변경된다" {
        val couponUsage = CouponUsage(
            userId = 1L,
            orderId = 1L,
            couponId = 1L,
            issuedCouponId = 1L,
            discountAmount = 1000L
        )
        couponUsage.commit()
        couponUsage.status shouldBe CouponUsage.CouponUsageStatus.USED
    }
})