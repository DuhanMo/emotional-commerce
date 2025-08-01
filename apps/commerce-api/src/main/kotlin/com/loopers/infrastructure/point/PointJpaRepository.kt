package com.loopers.infrastructure.point

import com.loopers.domain.point.Point
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PointJpaRepository : JpaRepository<Point, Long> {
    @Query("SELECT p FROM Point p JOIN User u ON p.userId = u.id WHERE u.loginId = :loginId")
    fun findByUserLoginId(loginId: String): Point?

    fun findByUserId(userId: Long): Point?
}
