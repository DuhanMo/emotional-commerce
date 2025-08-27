package com.loopers.domain.common.events

import com.loopers.domain.order.Address
import com.loopers.domain.order.Order
import com.loopers.domain.order.OrderInfo
import com.loopers.domain.support.Money

data class OrderCreatedEvent(
    val orderId: Long,
    val userId: Long,
    val orderNumber: String,
    val totalAmount: Money,
    val orderLines: List<OrderInfo.OrderLineInfo>,
    val deliveryAddress: Address,
    val issuedCouponId: Long?,
) : BaseDomainEvent() {
    override val eventType: String = "ORDER_CREATED"

    companion object {
        fun from(order: Order): OrderCreatedEvent {
            return OrderCreatedEvent(
                orderId = order.id,
                userId = order.userId,
                orderNumber = order.orderNumber,
                totalAmount = order.totalAmount,
                orderLines = order.orderLines.map { OrderInfo.OrderLineInfo.from(it) },
                deliveryAddress = order.deliveryAddress,
                issuedCouponId = order.issuedCouponId,
            )
        }
    }
}
