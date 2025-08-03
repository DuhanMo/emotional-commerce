package com.loopers.infrastructure.point

import com.loopers.domain.point.Point
import com.loopers.domain.point.PointRepository
import com.loopers.domain.user.LoginId
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Repository

@Repository
class PointRepositoryImpl(
    private val pointJpaRepository: PointJpaRepository,
) : PointRepository {
    override fun save(point: Point): Point = pointJpaRepository.save(point)

    override fun getByUserLoginId(loginId: LoginId): Point = pointJpaRepository.findByUserLoginId(loginId.value)
        ?: throw CoreException(ErrorType.NOT_FOUND, "포인트를 찾을 수 없습니다.(loginId: ${loginId.value})")

    override fun getByUserId(userId: Long): Point = pointJpaRepository.findByUserId(userId)
        ?: throw CoreException(ErrorType.NOT_FOUND, "포인트를 찾을 수 없습니다.(userId: $userId)")
}
