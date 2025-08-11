package com.loopers.infrastructure.coupon

import com.loopers.domain.coupon.Coupon
import jakarta.persistence.LockModeType
import jakarta.persistence.QueryHint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.data.repository.query.Param

interface CouponJpaRepository : JpaRepository<Coupon, Long> {
    @Query("SELECT c FROM Coupon c WHERE c.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000"))
    fun findByIdWithLock(@Param("id") id: Long): Coupon?
}
