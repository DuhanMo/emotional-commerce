package com.loopers.domain.payment

import com.loopers.domain.order.Order
import com.loopers.domain.user.User

interface PayProcessor {
    val support: PaymentMethod

    fun process(command: PayProcessCommand)
}
