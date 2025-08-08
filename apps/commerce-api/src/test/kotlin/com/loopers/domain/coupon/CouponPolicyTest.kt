package com.loopers.domain.coupon

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CouponPolicyTest : StringSpec({
    "정액제 정책은 고정금액을 차감하여 최종금액을 반환한다" {
        val orderAmount = 10_000L
        val discountValue = 2_000L
        val finalAmount =
            CouponPolicy.FIXED_AMOUNT.calculateDiscountedAmount(
                orderAmount = orderAmount,
                discountValue = discountValue,
            )

        finalAmount shouldBe 8_000L
    }
    "정률제 정책은 퍼센티지 금액을 차감하여 최종금액을 반환한다" {
        val orderAmount = 10_000L
        val discountValue = 5L
        val finalAmount =
            CouponPolicy.PERCENTAGE.calculateDiscountedAmount(orderAmount = orderAmount, discountValue = discountValue)

        finalAmount shouldBe 9_500L
    }
})
