package com.loopers.domain.coupon

import org.springframework.stereotype.Service

@Service
class CouponQueryService(
    private val issuedCouponRepository: IssuedCouponRepository,
    private val couponRepository: CouponRepository,
) {
    fun findByIssuedCouponId(issuedCouponId: Long?): Coupon? =
        issuedCouponId?.let { issuedCouponRepository.getById(issuedCouponId) }?.let {
            couponRepository.getById(it.couponId)
        }
}
