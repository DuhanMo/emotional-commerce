package com.commerce.duhan.db.point

import com.commerce.duhan.core.domain.point.Point
import com.commerce.duhan.core.domain.point.PointRepository
import org.springframework.stereotype.Repository

@Repository
class PointRepositoryImpl(
    private val jpaRepository: PointJpaRepository,
) : PointRepository {
    override fun save(point: Point): Point = jpaRepository.save(point)

    override fun getByUserId(userId: Long): Point = jpaRepository.findByUserId(userId)
        ?: throw NoSuchElementException("포인트를 찾을 수 없습니다. userId: $userId")
}
