package com.loopers.infrastructure.point

import com.loopers.domain.point.Point
import jakarta.persistence.LockModeType
import jakarta.persistence.QueryHint
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.data.repository.query.Param

interface PointJpaRepository : JpaRepository<Point, Long> {
    @Query("SELECT p FROM Point p JOIN User u ON p.userId = u.id WHERE u.loginId = :loginId")
    fun findByUserLoginId(loginId: String): Point?

    fun findByUserId(userId: Long): Point?

    @Query("SELECT p FROM Point p WHERE p.userId = :userId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000"))
    fun findByUserIdWithLock(@Param("userId") userId: Long): Point?
}
