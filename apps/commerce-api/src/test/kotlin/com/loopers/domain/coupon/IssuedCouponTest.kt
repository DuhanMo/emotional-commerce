package com.loopers.domain.coupon

import com.loopers.domain.coupon.IssuedCoupon.IssuedCouponStatus
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.assertThrows

class IssuedCouponTest : StringSpec({
    "이미 사용된 쿠폰은 사용할 수 없다" {
        val issuedCoupon = IssuedCoupon(
            userId = 1L,
            couponId = 1L,
            status = IssuedCouponStatus.USED_PENDING,
        )
        val exception = assertThrows<IllegalStateException> { issuedCoupon.pending() }
        exception.message shouldContain "사용할 수 없는 쿠폰입니다."
    }

    "쿠폰을 사용처리 하면 상태가 USED_PENDING 으로 변경된다" {
        val issuedCoupon = IssuedCoupon(
            userId = 1L,
            couponId = 1L,
            status = IssuedCouponStatus.AVAILABLE,
        )
        issuedCoupon.pending()
        issuedCoupon.status shouldBe IssuedCouponStatus.USED_PENDING
    }
})
