package com.loopers.infrastructure.point

import com.loopers.domain.point.Point
import com.loopers.domain.point.PointRepository
import com.loopers.domain.user.LoginId
import org.springframework.stereotype.Repository

@Repository
class PointRepositoryImpl(
    private val pointJpaRepository: PointJpaRepository,
) : PointRepository {
    override fun save(point: Point): Point = pointJpaRepository.save(point)

    override fun findByUserLoginId(loginId: LoginId): Point? = pointJpaRepository.findByUserLoginId(loginId.value)
}
