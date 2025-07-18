package com.loopers.domain.user

data class ChargePointCommand(
    val loginId: LoginId,
    val point: Int,
)
