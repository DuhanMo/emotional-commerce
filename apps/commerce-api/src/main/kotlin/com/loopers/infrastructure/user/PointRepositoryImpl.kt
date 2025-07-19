package com.loopers.infrastructure.user

import com.loopers.domain.user.Point
import com.loopers.domain.user.PointRepository
import org.springframework.stereotype.Repository

@Repository
class PointRepositoryImpl(
    private val pointJpaRepository: PointJpaRepository,
) : PointRepository {
    override fun findByUserId(userId: Long): Point? = pointJpaRepository.findByUserId(userId)

    override fun save(point: Point): Point = pointJpaRepository.save(point)
}
