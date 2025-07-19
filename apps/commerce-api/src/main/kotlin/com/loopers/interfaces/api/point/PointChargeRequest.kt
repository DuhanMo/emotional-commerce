package com.loopers.interfaces.api.point

import com.loopers.domain.user.ChargePointCommand
import com.loopers.domain.user.LoginId
import jakarta.validation.constraints.Min

data class PointChargeRequest(
    @field:Min(value = 1, message = "충전 금액은 1원 이상이어야 합니다.")
    val point: Int?,
) {
    fun toCommand(loginId: String): ChargePointCommand = ChargePointCommand(
        loginId = LoginId(loginId),
        point = point!!,
    )
}
