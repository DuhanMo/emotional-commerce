package com.commerce.duhan.api.point

import com.commerce.duhan.application.point.PointResult
import com.commerce.duhan.domain.common.Money
import jakarta.validation.constraints.Min

data class ChargePointRequest(
    @field:Min(value = 1, message = "충전 금액은 1원 이상이어야 합니다.")
    val amount: Money?,
)

data class PointResponse(
    val id: Long,
    val amount: Money,
) {
    companion object {
        fun from(result: PointResult): PointResponse = PointResponse(
            id = result.id,
            amount = result.amount,
        )
    }
}
