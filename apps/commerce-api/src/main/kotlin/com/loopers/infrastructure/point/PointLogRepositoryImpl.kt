package com.loopers.infrastructure.point

import com.loopers.domain.point.PointLog
import com.loopers.domain.point.PointLogRepository
import org.springframework.stereotype.Repository

@Repository
class PointLogRepositoryImpl(
    private val pointLogJpaRepository: PointLogJpaRepository,
) : PointLogRepository {
    override fun save(pointLog: PointLog): PointLog = pointLogJpaRepository.save(pointLog)
}
