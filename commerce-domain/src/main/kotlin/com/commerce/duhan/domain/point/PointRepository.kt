package com.commerce.duhan.domain.point

interface PointRepository {
    fun save(point: Point): Point

    fun getByUserId(userId: Long): Point

    fun getByUserIdWithLock(userId: Long): Point
}
