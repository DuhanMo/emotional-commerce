package com.loopers.infrastructure.coupon

import com.loopers.domain.coupon.IssuedCoupon
import com.loopers.domain.coupon.IssuedCouponRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class IssuedCouponRepositoryImpl(
    private val issuedCouponJpaRepository: IssuedCouponJpaRepository,
) : IssuedCouponRepository {
    override fun save(issuedCoupon: IssuedCoupon): IssuedCoupon =
        issuedCouponJpaRepository.save(issuedCoupon)

    override fun getById(id: Long): IssuedCoupon = issuedCouponJpaRepository.findByIdOrNull(id)
        ?: throw NoSuchElementException("발급쿠폰을 찾을 수 없습니다.(id: $id)")
}
