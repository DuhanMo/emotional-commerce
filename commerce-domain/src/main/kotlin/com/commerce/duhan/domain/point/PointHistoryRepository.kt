package com.commerce.duhan.domain.point

interface PointHistoryRepository {
    fun save(pointHistory: PointHistory): PointHistory
}
