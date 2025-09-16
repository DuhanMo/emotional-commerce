package com.commerce.duhan.db.point

import com.commerce.duhan.domain.point.Point
import com.commerce.duhan.domain.point.PointRepository
import org.springframework.stereotype.Repository

@Repository
class PointRepositoryImpl(
    private val jpaRepository: PointJpaRepository,
) : PointRepository {
    override fun save(point: Point): Point = jpaRepository.save(point)

    override fun getByUserId(userId: Long): Point = jpaRepository.findByUserId(userId)
        ?: throw NoSuchElementException("포인트를 찾을 수 없습니다. userId: $userId")

    override fun getByUserIdWithLock(userId: Long): Point = jpaRepository.findByUserIdWithLock(userId)
        ?: throw NoSuchElementException("포인트를 찾을 수 없습니다. userId: $userId")
}
