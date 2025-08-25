package com.loopers.domain.coupon

import com.loopers.domain.support.Money
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CouponPolicyTest : StringSpec({
    "정액제 정책은 고정할인금액이 주문금액보다 큰 경우 주문금액을 반환한다" {
        val orderAmount = Money(10_000L)
        val discountValue = 12_000L
        val finalAmount =
            CouponPolicy.FIXED_AMOUNT.calculateDiscountPrice(
                orderAmount = orderAmount,
                discountValue = discountValue,
            )

        finalAmount shouldBe Money(10_000L)
    }

    "정액제 정책은 고정할인금액이 주문금액보다 작거나 같은 경우 고정할인금액 반환한다" {
        val orderAmount = Money(10_000)
        val discountValue = 2_000L
        val finalAmount =
            CouponPolicy.FIXED_AMOUNT.calculateDiscountPrice(
                orderAmount = orderAmount,
                discountValue = discountValue,
            )

        finalAmount shouldBe Money(2_000L)
    }

    "정률제 정책은 퍼센티지 금액을 적용하여 할인금액을 반환한다" {
        val orderAmount = Money(10_000L)
        val discountValue = 5L
        val finalAmount =
            CouponPolicy.PERCENTAGE.calculateDiscountPrice(orderAmount = orderAmount, discountValue = discountValue)

        finalAmount shouldBe Money(500)
    }
})
