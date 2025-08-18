package com.loopers.domain.order

import com.loopers.application.order.CaptureOrderInfoCommand
import org.springframework.stereotype.Service

@Service
class OrderService(
    private val orderRepository: OrderRepository,
) {
    fun createOrder(command: CreateOrderCommand): Order {
        val orderLines = command.orderLines.map { item ->
            OrderLine(
                productId = item.productId,
                quantity = item.quantity,
                unitPrice = item.unitPrice,
            )
        }
        val order = Order(
            userId = command.userId,
        ).apply {
            addOrderLines(orderLines)
        }

        return orderRepository.save(order)
    }

    fun captureOrderInfo(command: CaptureOrderInfoCommand): Order {
        command.orderId
            val order = orderRepository.findById(orderId)
                ?: throw IllegalArgumentException("존재하지 않는 주문입니다: $orderId")
            order.checkout(
                amount = amount,
                issuedCouponId = issuedCouponId,
                deliveryAddress = deliveryAddress,
            )
            orderRepository.save(order)
    }
}
