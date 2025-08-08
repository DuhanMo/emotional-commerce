package com.loopers.domain.point

interface PointHistoryRepository {
    fun save(pointHistory: PointHistory): PointHistory
}
