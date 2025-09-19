package com.commerce.duhan.application.point

import com.commerce.duhan.domain.common.Money
import com.commerce.duhan.domain.point.Point
import com.commerce.duhan.domain.point.PointHistory
import com.commerce.duhan.domain.point.PointHistoryRepository
import com.commerce.duhan.domain.point.PointRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PointCharger(
    private val pointRepository: PointRepository,
    private val pointHistoryRepository: PointHistoryRepository,
) {
    @Transactional
    fun charge(userId: Long, amount: Money): Point {
        val point = pointRepository.getByUserIdWithLock(userId)
        point.charge(amount)
        pointHistoryRepository.save(
            PointHistory.fromCharge(
                userId = userId,
                pointId = point.id,
                amount = amount,
            ),
        )
        return point
    }
}
