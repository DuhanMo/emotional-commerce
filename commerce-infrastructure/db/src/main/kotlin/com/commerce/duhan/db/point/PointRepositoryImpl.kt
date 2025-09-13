package com.commerce.duhan.db.point

import com.commerce.duhan.core.domain.point.Point
import com.commerce.duhan.core.domain.point.PointRepository
import org.springframework.stereotype.Repository

@Repository
class PointRepositoryImpl(
    private val pointJpaRepository: PointJpaRepository,
) : PointRepository {
    override fun save(point: Point): Point = pointJpaRepository.save(point)
}
