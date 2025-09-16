package com.commerce.duhan.application.point

import com.commerce.duhan.application.user.UserFinder
import com.commerce.duhan.domain.common.Money
import org.springframework.stereotype.Service

@Service
class PointApplicationService(
    private val userFinder: UserFinder,
    private val pointCharger: PointCharger,
    private val pointFinder: PointFinder,
) {
    fun charge(userId: Long, amount: Money): PointResult {
        val user = userFinder.getById(userId)
        val point = pointCharger.charge(user.id, amount)
        return PointResult.from(point)
    }

    fun find(userId: Long): PointResult {
        val user = userFinder.getById(userId)
        val point = pointFinder.getByUserId(user.id)
        return PointResult.from(point)
    }
}
