package com.loopers.domain.point

import com.loopers.domain.user.LoginId
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Component

@Component
class PointQueryService(
    private val pointRepository: PointRepository,
) {
    fun getByUserLoginId(loginId: LoginId): Point = pointRepository.findByUserLoginId(loginId)
        ?: throw CoreException(ErrorType.NOT_FOUND, "존재하지 않는 포인트입니다")
}