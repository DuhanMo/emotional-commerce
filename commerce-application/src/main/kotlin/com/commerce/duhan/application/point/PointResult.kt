package com.commerce.duhan.application.point

import com.commerce.duhan.domain.common.Money
import com.commerce.duhan.domain.point.Point

data class PointResult(
    val id: Long,
    val amount: Money,
) {
    companion object {
        fun from(point: Point): PointResult = PointResult(
            id = point.id,
            amount = point.amount,
        )
    }
}
