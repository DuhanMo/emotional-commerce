package com.loopers.domain.user

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PointWriter(
    private val pointRepository: PointRepository,
) {
    @Transactional
    fun write(point: Point): Point = pointRepository.save(point)
}
