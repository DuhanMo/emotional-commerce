package com.loopers.domain.coupon

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class IssuedCouponService(
    private val issuedCouponRepository: IssuedCouponRepository,
    private val couponUsageRepository: CouponUsageRepository,
) {
    @Transactional
    fun pendingCoupon(userId: Long, issuedCouponId: Long) {
        val issuedCoupon = issuedCouponRepository.getById(issuedCouponId)
        require(issuedCoupon.userId == userId) { "쿠폰을 사용할 수 없습니다." }

        issuedCoupon.pending()

        issuedCouponRepository.save(issuedCoupon)
    }

    fun commit(orderId: Long) {
        val (couponUsage, issuedCoupon) = getCouponUsageAndIssuedCoupon(orderId)

        couponUsage.commit()
        issuedCoupon.commit()

        couponUsageRepository.save(couponUsage)
        issuedCouponRepository.save(issuedCoupon)
    }

    fun release(orderId: Long) {
        val (couponUsage, issuedCoupon) = getCouponUsageAndIssuedCoupon(orderId)

        couponUsage.release()
        issuedCoupon.release()

        couponUsageRepository.save(couponUsage)
        issuedCouponRepository.save(issuedCoupon)
    }

    private fun getCouponUsageAndIssuedCoupon(orderId: Long): Pair<CouponUsage, IssuedCoupon> {
        val couponUsage = couponUsageRepository.getByOrderId(orderId)
        val issuedCoupon = issuedCouponRepository.getById(couponUsage.issuedCouponId)
        return Pair(couponUsage, issuedCoupon)
    }
}
