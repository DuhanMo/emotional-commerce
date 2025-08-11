package com.loopers.domain.coupon

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "coupon_usage")
@Entity
class CouponUsage(
    private val orderId: Long,
    private val couponId: Long,
    private val issuedCouponId: Long,
    private val discountAmount: Long,
) : BaseEntity()
