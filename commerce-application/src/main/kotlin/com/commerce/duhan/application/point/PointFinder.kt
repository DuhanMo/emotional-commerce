package com.commerce.duhan.application.point

import com.commerce.duhan.domain.point.Point
import com.commerce.duhan.domain.point.PointRepository
import org.springframework.stereotype.Component

@Component
class PointFinder(
    private val pointRepository: PointRepository,
) {
    fun getByUserId(userId: Long): Point = pointRepository.getByUserId(userId)
}
