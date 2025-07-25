package com.loopers.domain.point

import com.loopers.domain.user.LoginId

data class ChargePointCommand(
    val loginId: LoginId,
    val point: Int,
)
