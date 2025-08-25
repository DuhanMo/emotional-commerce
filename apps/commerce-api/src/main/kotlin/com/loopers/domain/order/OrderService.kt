package com.loopers.domain.order

import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
) {
    fun createOrder(command: CreateOrderCommand): Order {
        // TODO: 상품, 쿠폰, 금액 등을 검증
        val order = Order(
            userId = command.userId,
            totalAmount = command.totalAmount,
            deliveryAddress = command.deliveryAddress,
        ).apply {
            addOrderLines(command.orderLines)
        }
        command.coupon?.let {
            order.couponId = it.id
        }

        return orderRepository.save(order)
    }

    fun paid(order: Order) {
        order.paid()
        orderRepository.save(order)
    }

    fun payFail(order: Order) {
        order.payFail()
        orderRepository.save(order)
    }

    fun error(order: Order) {
        order.error()
        orderRepository.save(order)
    }
}
