package com.loopers.domain.payment

interface PayProcessor {
    fun support(): PaymentMethod

    fun process(command: PayProcessCommand)
}
