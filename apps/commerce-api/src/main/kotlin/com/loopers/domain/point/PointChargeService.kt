package com.loopers.domain.point

import com.loopers.domain.support.Money
import org.springframework.stereotype.Service

@Service
class PointChargeService(
    private val pointRepository: PointRepository,
    private val pointHistoryRepository: PointHistoryRepository,
) {
    fun charge(point: Point, amount: Money): Point {
        point.charge(amount)

        pointHistoryRepository.save(PointHistory.fromCharge(point.userId, point.id, amount))
        return pointRepository.save(point)
    }
}
