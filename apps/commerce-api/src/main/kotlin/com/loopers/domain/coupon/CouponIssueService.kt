package com.loopers.domain.coupon

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CouponIssueService(
    private val couponRepository: CouponRepository,
    private val issuedCouponRepository: IssuedCouponRepository,
) {
    @Transactional
    fun issue(userId: Long, couponId: Long) {
        val coupon = couponRepository.getByIdWithLock(couponId)

        coupon.issue()

        issuedCouponRepository.save(IssuedCoupon(userId, coupon.id))
        couponRepository.save(coupon)
    }
}
