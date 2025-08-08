package com.loopers.infrastructure.coupon

import com.loopers.domain.coupon.Coupon
import com.loopers.domain.coupon.CouponRepository
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class CouponRepositoryImpl(
    private val couponJpaRepository: CouponJpaRepository,
) : CouponRepository {
    override fun save(coupon: Coupon): Coupon = couponJpaRepository.save(coupon)

    override fun getByIdWithLock(id: Long): Coupon = couponJpaRepository.findByIdWithLock(id)
        ?: throw CoreException(ErrorType.NOT_FOUND, "쿠폰을 찾을 수 없습니다.(couponId: $id)")

    override fun getById(id: Long): Coupon = couponJpaRepository.findByIdOrNull(id)
        ?: throw CoreException(ErrorType.NOT_FOUND, "쿠폰을 찾을 수 없습니다.(couponId: $id)")
}
