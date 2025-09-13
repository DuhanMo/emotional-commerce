package com.commerce.duhan.core.domain.point

interface PointRepository {
    fun save(point: Point): Point
}
