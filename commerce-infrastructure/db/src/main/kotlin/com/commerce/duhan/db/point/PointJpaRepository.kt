package com.commerce.duhan.db.point

import com.commerce.duhan.core.domain.point.Point
import org.springframework.data.jpa.repository.JpaRepository

interface PointJpaRepository : JpaRepository<Point, Long> {
    fun findByUserId(userId: Long): Point?
}
