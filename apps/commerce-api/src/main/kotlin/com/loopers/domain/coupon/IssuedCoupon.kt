package com.loopers.domain.coupon

import com.loopers.domain.BaseEntity
import com.loopers.domain.coupon.IssuedCoupon.IssuedCouponStatus.AVAILABLE
import com.loopers.domain.coupon.IssuedCoupon.IssuedCouponStatus.RELEASED
import com.loopers.domain.coupon.IssuedCoupon.IssuedCouponStatus.USED_PENDING
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.time.Instant

@Table(name = "issued_coupon")
@Entity
class IssuedCoupon(
    val userId: Long,
    val couponId: Long,
    @Enumerated(EnumType.STRING)
    var status: IssuedCouponStatus = AVAILABLE,
    var usedAt: Instant? = null,
    val issuedAt: Instant = Instant.now(),
    val expiresAt: Instant? = null,
) : BaseEntity() {
    @Version
    var version: Long = 0L

    fun use() {
        check(status in listOf(AVAILABLE, RELEASED)) { "사용할 수 없는 쿠폰입니다." }

        this.status = USED_PENDING
        this.usedAt = Instant.now()
    }

    enum class IssuedCouponStatus {
        AVAILABLE,
        USED_PENDING,
        USED,
        EXPIRED,
        RELEASED,
    }
}
