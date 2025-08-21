package com.loopers.infrastructure.coupon

import com.loopers.domain.coupon.CouponUsage
import com.loopers.domain.coupon.CouponUsageRepository
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Repository

@Repository
class CouponUsageRepositoryImpl(
    private val jpaRepository: CouponUsageJpaRepository,
) : CouponUsageRepository {
    override fun getByOrderId(orderId: Long): CouponUsage = jpaRepository.findByOrderId(orderId)
        ?: throw CoreException(ErrorType.NOT_FOUND, "사용쿠폰을 찾을 수 없습니다.(orderId: $orderId)")

    override fun save(couponUsage: CouponUsage): CouponUsage = jpaRepository.save(couponUsage)
}
