package com.loopers.application.user

import com.loopers.domain.user.Point

data class PointOutput(
    val point: Int,
) {
    companion object {
        fun from(point: Point): PointOutput = PointOutput(point.amount)
    }
}
