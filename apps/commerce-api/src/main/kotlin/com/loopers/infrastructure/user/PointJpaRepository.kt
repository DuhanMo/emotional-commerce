package com.loopers.infrastructure.user

import com.loopers.domain.point.Point
import org.springframework.data.jpa.repository.JpaRepository

interface PointJpaRepository : JpaRepository<Point, Long> {
    fun findByUserId(userId: Long): Point?
}
