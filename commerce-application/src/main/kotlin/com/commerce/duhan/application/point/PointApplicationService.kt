package com.commerce.duhan.application.point

import com.commerce.duhan.domain.common.Money
import org.springframework.stereotype.Service

@Service
class PointApplicationService(
    private val pointCharger: PointCharger,
) {
    fun charge(userId: Long, amount: Money): PointChargeResult {
        val point = pointCharger.charge(userId, amount)
        return PointChargeResult(point.amount)
    }
}
