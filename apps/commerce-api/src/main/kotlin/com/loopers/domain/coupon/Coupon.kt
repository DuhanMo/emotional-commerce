package com.loopers.domain.coupon

import com.loopers.domain.BaseEntity
import com.loopers.domain.support.Money
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "coupon")
@Entity
class Coupon(
    val name: String,
    val maxIssueCount: Long,
    @Enumerated(EnumType.STRING)
    val policy: CouponPolicy,
    val discountValue: Long,
    var issuedCount: Long = 0L,
    id: Long = 0L,
) : BaseEntity(id) {
    fun issue(): Long {
        require(canIssue()) { "쿠폰을 발급할 수 없습니다." }
        issuedCount++
        return id
    }

    fun calculateDiscountPrice(orderAmount: Money): Money =
        policy.calculateDiscountPrice(orderAmount, discountValue)

    private fun canIssue(): Boolean = remainCount > 0

    private val remainCount: Long
        get() = maxIssueCount - issuedCount
}
