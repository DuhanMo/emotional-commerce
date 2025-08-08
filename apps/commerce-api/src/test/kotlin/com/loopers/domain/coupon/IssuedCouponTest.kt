package com.loopers.domain.coupon

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.assertThrows

class IssuedCouponTest : StringSpec({
    "이미 사용된 쿠폰은 사용할 수 없다" {
        val issuedCoupon = IssuedCoupon(
            userId = 1L,
            couponId = 1L,
            status = IssuedCouponStatus.USED,
        )
        val exception = assertThrows<IllegalArgumentException> { issuedCoupon.use() }
        exception.message shouldContain "이미 사용된 쿠폰입니다."
    }
})
