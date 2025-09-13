package com.commerce.duhan.core.application.point

import com.commerce.duhan.core.domain.point.Point
import com.commerce.duhan.core.domain.point.PointRepository
import org.springframework.stereotype.Component

@Component
class PointCreator(
    private val pointRepository: PointRepository,
) {
    fun create(userId: Long) {
        pointRepository.save(Point(userId))
    }
}
