package com.loopers.support.fixture

import com.loopers.domain.point.Point
import com.loopers.domain.point.PointHistory
import com.loopers.domain.point.PointHistory.PointHistoryType
import com.loopers.domain.support.Money

fun createPoint(
    userId: Long = 1L,
    amount: Money = Money.ZERO,
    id: Long = 1L,
) = Point(
    userId = userId,
    amount = amount,
    id = id,
)

fun createPointHistory(
    userId: Long = 1L,
    pointId: Long = 1L,
    type: PointHistoryType = PointHistoryType.CHARGE,
    amount: Money = Money(100),
) = PointHistory(
    userId = userId,
    pointId = pointId,
    type = type,
    amount = amount,
)
