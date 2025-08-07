package com.loopers.domain.coupon

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertThrows

class CouponTest : StringSpec({
    "발급수량을 초과하면 발급 시 예외발생한다" {
        val coupon = Coupon(
            name = "테스트 쿠폰",
            maxIssueCount = 10L,
            policy = CouponPolicy.FIXED_AMOUNT,
            discountValue = 2_000L,
            issuedCount = 10L,
        )
        assertThrows<IllegalArgumentException> {
            coupon.issue()
        }
    }

    "쿠폰을 발급하면 발급수량이 1 증가한다" {
        val coupon = Coupon(
            name = "테스트 쿠폰",
            maxIssueCount = 10L,
            policy = CouponPolicy.FIXED_AMOUNT,
            discountValue = 2_000L,
            issuedCount = 0L,
        )
        coupon.issue()

        coupon.issuedCount shouldBe 1L
    }

    "쿠폰을 발급하면 쿠폰의 식별자를 반환한다" {
        val coupon = Coupon(
            name = "테스트 쿠폰",
            maxIssueCount = 10L,
            policy = CouponPolicy.FIXED_AMOUNT,
            discountValue = 2_000L,
            issuedCount = 0L,
            id = 99L,
        )
        val result = coupon.issue()

        result shouldBe 99L
    }

    "정액제 정책은 고정금액을 차감하여 최종금액을 반환한다" {
        val orderAmount = 10_000L
        val fixedAmountPolicy = CouponPolicy.FIXED_AMOUNT
        val coupon = Coupon(
            name = "테스트 쿠폰",
            maxIssueCount = 10L,
            policy = fixedAmountPolicy,
            discountValue = 2_000L,
            issuedCount = 0L,
            id = 99L,
        )

        coupon.calculateDiscountedAmount(orderAmount) shouldBe 8_000L
    }
    "정률제 정책은 퍼센티지 금액을 차감하여 최종금액을 반환한다" {
        val orderAmount = 10_000L
        val percentagePolicy = CouponPolicy.PERCENTAGE
        val coupon = Coupon(
            name = "테스트 쿠폰",
            maxIssueCount = 10L,
            policy = percentagePolicy,
            discountValue = 5L,
            issuedCount = 0L,
            id = 99L,
        )
        coupon.calculateDiscountedAmount(orderAmount) shouldBe 9_500L
    }
})
