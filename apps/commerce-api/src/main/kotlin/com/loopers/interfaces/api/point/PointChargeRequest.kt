package com.loopers.interfaces.api.point

import jakarta.validation.constraints.Min

data class PointChargeRequest(
    @field:Min(value = 1, message = "충전 금액은 1원 이상이어야 합니다.")
    val point: Long?,
)
