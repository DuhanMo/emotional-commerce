package com.loopers.domain.point

interface PointLogRepository {
    fun save(pointLog: PointLog): PointLog
}
