package com.loopers.domain.point

import org.springframework.stereotype.Service

@Service
class PointChargeService(
    private val pointRepository: PointRepository,
    private val pointLogRepository: PointLogRepository,
) {
    fun charge(point: Point, amount: Long): Point {
        point.charge(amount)

        pointLogRepository.save(
            PointLog(
                userId = point.userId,
                pointId = point.id,
                amount = amount,
            ),
        )
        return pointRepository.save(point)
    }
}
