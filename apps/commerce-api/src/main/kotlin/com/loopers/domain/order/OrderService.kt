package com.loopers.domain.order

import com.loopers.domain.common.events.DomainEventPublisher
import com.loopers.domain.common.events.OrderCreatedEvent
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val eventPublisher: DomainEventPublisher,
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

        command.issuedCouponId?.let {
            order.issuedCouponId = it
        }
        eventPublisher.publish(OrderCreatedEvent.from(order))
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

    fun payRequest(order: Order) {
        order.payRequest()
        orderRepository.save(order)
    }
}
