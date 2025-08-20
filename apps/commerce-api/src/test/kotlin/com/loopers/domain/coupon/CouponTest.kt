package com.loopers.domain.coupon

import com.loopers.domain.support.Money
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

    "정액제 정책은 고정할인금액이 주문금액보다 큰 경우 주문금액을 반환한다" {
        val orderAmount = Money(10_000L)
        val fixedAmountPolicy = CouponPolicy.FIXED_AMOUNT
        val coupon = Coupon(
            name = "테스트 쿠폰",
            maxIssueCount = 10L,
            policy = fixedAmountPolicy,
            discountValue = 2_000L,
            issuedCount = 0L,
            id = 99L,
        )

        coupon.calculateDiscountPrice(orderAmount) shouldBe Money(2_000L)
    }

    "정액제 정책은 고정할인금액이 주문금액보다 작거나 같은 경우 주문금액을 반환한다" {
        val orderAmount = Money(10_000L)
        val fixedAmountPolicy = CouponPolicy.FIXED_AMOUNT
        val coupon = Coupon(
            name = "테스트 쿠폰",
            maxIssueCount = 10L,
            policy = fixedAmountPolicy,
            discountValue = 12_000L,
            issuedCount = 0L,
            id = 99L,
        )

        coupon.calculateDiscountPrice(orderAmount) shouldBe Money(10_000L)
    }

    "정률제 정책은 주문금액에 퍼센티지를 적용하여 할인가격을 반환한다" {
        val orderAmount = Money(10_000L)
        val percentagePolicy = CouponPolicy.PERCENTAGE
        val coupon = Coupon(
            name = "테스트 쿠폰",
            maxIssueCount = 10L,
            policy = percentagePolicy,
            discountValue = 5L,
            issuedCount = 0L,
            id = 99L,
        )
        coupon.calculateDiscountPrice(orderAmount) shouldBe Money(500L)
    }
})
