package com.loopers.interfaces.api.point

import com.loopers.application.user.PointOutput

data class PointResponse(
    val point: Int,
) {
    companion object {
        fun from(output: PointOutput): PointResponse = PointResponse(output.point)
    }
}
