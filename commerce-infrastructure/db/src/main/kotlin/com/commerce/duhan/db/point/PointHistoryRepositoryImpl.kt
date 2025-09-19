package com.commerce.duhan.db.point

import com.commerce.duhan.domain.point.PointHistory
import com.commerce.duhan.domain.point.PointHistoryRepository
import org.springframework.stereotype.Repository

@Repository
class PointHistoryRepositoryImpl(
    private val jpaRepository: PointHistoryJpaRepository,
) : PointHistoryRepository {
    override fun save(pointHistory: PointHistory): PointHistory = jpaRepository.save(pointHistory)
}
