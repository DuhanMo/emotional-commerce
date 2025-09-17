package com.commerce.duhan.domain.fixtures

import com.commerce.duhan.domain.common.Money
import com.commerce.duhan.domain.point.Point
import com.commerce.duhan.domain.point.PointHistory
import com.commerce.duhan.domain.point.PointHistoryType

fun createPoint(
    userId: Long = 1L,
    amount: Money = 0,
    id: Long = 0L,
): Point = Point(
    userId = userId,
    amount = amount,
    id = id,
)

fun createPointHistory(
    userId: Long = 1L,
    pointId: Long = 1L,
    type: PointHistoryType = PointHistoryType.CHARGE,
    amount: Money = 100,
): PointHistory = PointHistory(
    userId = userId,
    pointId = pointId,
    type = type,
    amount = amount,
)
