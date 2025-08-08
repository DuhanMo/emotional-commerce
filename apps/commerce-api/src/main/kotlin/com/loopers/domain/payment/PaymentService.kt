package com.loopers.domain.payment

import com.loopers.domain.order.Order
import com.loopers.domain.order.OrderRepository
import com.loopers.domain.user.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService(
    private val orderRepository: OrderRepository,
    payProcessors: List<PayProcessor>,
) {
    private val payProcessors = payProcessors.associateBy { it.support }

    @Transactional
    fun pay(user: User, order: Order) {
        val payProcessor = payProcessors[order.payMethod]
            ?: throw IllegalArgumentException("지원하지 않는 결제 방법입니다: ${order.payMethod}")

        payProcessor.process(user, order)
        order.paid()

        orderRepository.save(order)
    }
}
