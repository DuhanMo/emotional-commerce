package com.loopers.domain.coupon

import com.loopers.domain.BaseEntity
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
    var status: IssuedCouponStatus = IssuedCouponStatus.ACTIVE,
    var usedAt: Instant? = null,
    val issuedAt: Instant = Instant.now(),
) : BaseEntity() {
    @Version
    var version: Long = 0L

    fun use() {
        require(this.status != IssuedCouponStatus.USED) { "이미 사용된 쿠폰입니다." }
        this.status = IssuedCouponStatus.USED
        this.usedAt = Instant.now()
    }
}
