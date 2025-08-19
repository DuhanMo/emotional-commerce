package com.loopers.domain.payment

interface PayProcessor {
    val support: PaymentMethod

    fun process(command: PayProcessCommand)
}
