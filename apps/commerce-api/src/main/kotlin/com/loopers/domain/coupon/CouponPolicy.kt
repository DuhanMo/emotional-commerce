package com.loopers.domain.coupon

enum class CouponPolicy {
    FIXED_AMOUNT {
        override fun calculateDiscountedAmount(orderAmount: Long, discountValue: Long): Long =
            orderAmount - minOf(orderAmount, discountValue)
    },
    PERCENTAGE {
        override fun calculateDiscountedAmount(orderAmount: Long, discountValue: Long): Long =
            orderAmount - (orderAmount * discountValue / 100)
    },
    ;

    abstract fun calculateDiscountedAmount(orderAmount: Long, discountValue: Long): Long
}
