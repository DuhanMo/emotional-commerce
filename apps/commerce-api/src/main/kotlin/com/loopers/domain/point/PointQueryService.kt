package com.loopers.domain.point

import com.loopers.domain.user.LoginId
import org.springframework.stereotype.Service

@Service
class PointQueryService(
    private val pointRepository: PointRepository,
) {
    fun getByUserLoginId(loginId: LoginId): Point = pointRepository.getByUserLoginId(loginId)
}
