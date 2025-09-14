package com.commerce.duhan.core.application.point

import com.commerce.duhan.core.domain.point.Point
import com.commerce.duhan.core.domain.point.PointRepository
import org.springframework.stereotype.Component

@Component
class PointFinder(
    private val pointRepository: PointRepository,
) {
    fun getByUserId(userId: Long): Point = pointRepository.getByUserId(userId)
}
