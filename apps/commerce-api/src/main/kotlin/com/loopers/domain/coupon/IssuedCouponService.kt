package com.loopers.domain.coupon

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class IssuedCouponService(
    private val issuedCouponRepository: IssuedCouponRepository,
) {
    @Transactional
    fun pendingCoupon(userId: Long, issuedCouponId: Long) {
        val issuedCoupon = issuedCouponRepository.getById(issuedCouponId)
        require(issuedCoupon.userId == userId) { "쿠폰을 사용할 수 없습니다." }

        issuedCoupon.pending()

        issuedCouponRepository.save(issuedCoupon)
    }
}
