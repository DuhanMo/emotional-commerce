package com.loopers.application.point

import com.loopers.domain.point.Point
import com.loopers.domain.support.Money

data class PointOutput(
    val point: Money,
) {
    companion object {
        fun from(point: Point): PointOutput = PointOutput(point.amount)
    }
}
