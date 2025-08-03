package com.loopers.domain.payment

import com.loopers.domain.order.OrderInfo
import com.loopers.domain.user.User

data class PayCommand(
    val user: User,
    val order: OrderInfo,
)
