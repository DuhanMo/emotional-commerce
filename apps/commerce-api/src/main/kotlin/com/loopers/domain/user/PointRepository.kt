package com.loopers.domain.user

interface PointRepository {
    fun findByUserId(userId: Long): Point?

    fun save(point: Point): Point
}
