package com.loopers.domain.coupon

import com.loopers.domain.support.Money

enum class CouponPolicy {
    FIXED_AMOUNT {
        override fun calculateDiscountPrice(orderAmount: Money, discountValue: Long): Money =
            if (Money(discountValue) > orderAmount) {
                orderAmount
            } else {
                Money(discountValue)
            }
    },
    PERCENTAGE {
        override fun calculateDiscountPrice(orderAmount: Money, discountValue: Long): Money =
            orderAmount * discountValue / 100
    },
    ;

    abstract fun calculateDiscountPrice(orderAmount: Money, discountValue: Long): Money
}
