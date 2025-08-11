package com.loopers.application.point

import com.loopers.domain.user.LoginId

data class ChargePointInput(
    val loginId: LoginId,
    val point: Long,
)
