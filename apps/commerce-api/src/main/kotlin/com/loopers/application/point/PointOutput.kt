package com.loopers.application.point

import com.loopers.domain.point.Point

data class PointOutput(
    val point: Int,
) {
    companion object {
        fun from(point: Point): PointOutput = PointOutput(point.amount)
    }
}
