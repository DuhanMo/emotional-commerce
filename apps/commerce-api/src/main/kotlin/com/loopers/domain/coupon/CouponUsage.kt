package com.loopers.domain.coupon

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "coupon_usage")
@Entity
class CouponUsage(
    val userId: Long,
    val orderId: Long,
    val couponId: Long,
    val issuedCouponId: Long,
    val discountAmount: Long,
    @Enumerated(EnumType.STRING)
    var status: CouponUsageStatus = CouponUsageStatus.USED_PENDING,
) : BaseEntity() {

    fun commit() {
        status = CouponUsageStatus.USED
    }

    enum class CouponUsageStatus {
        USED_PENDING,
        USED,
        RELEASED,
    }
}
