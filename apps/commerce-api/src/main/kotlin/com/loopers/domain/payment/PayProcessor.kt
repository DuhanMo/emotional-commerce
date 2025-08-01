package com.loopers.domain.payment

import com.loopers.domain.order.Order
import com.loopers.domain.order.PayMethod
import com.loopers.domain.user.User

interface PayProcessor {
    val support: PayMethod

    fun process(user: User, order: Order)
}
