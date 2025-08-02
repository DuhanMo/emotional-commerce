package com.loopers.application.order

import com.loopers.domain.user.LoginId

data class PayInput(
    val loginId: LoginId,
    val orderId: Long,
)
