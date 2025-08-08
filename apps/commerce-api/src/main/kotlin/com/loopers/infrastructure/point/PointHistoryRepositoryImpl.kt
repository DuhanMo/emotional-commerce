package com.loopers.infrastructure.point

import com.loopers.domain.point.PointHistory
import com.loopers.domain.point.PointHistoryRepository
import org.springframework.stereotype.Repository

@Repository
class PointHistoryRepositoryImpl(
    private val pointHistoryJpaRepository: PointHistoryJpaRepository,
) : PointHistoryRepository {
    override fun save(pointHistory: PointHistory): PointHistory = pointHistoryJpaRepository.save(pointHistory)
}
