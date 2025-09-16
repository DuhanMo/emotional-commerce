package com.commerce.duhan.application.point

import com.commerce.duhan.domain.point.Point
import com.commerce.duhan.domain.point.PointRepository
import org.springframework.stereotype.Component

@Component
class PointCreator(
    private val pointRepository: PointRepository,
) {
    fun create(userId: Long) {
        pointRepository.save(Point(userId))
    }
}
