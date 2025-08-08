package com.loopers.domain.order

import com.loopers.domain.coupon.Coupon
import com.loopers.domain.order.OrderInfo.OrderLineInfo

data class CreateOrderCommand(
    val userId: Long,
    val deliveryAddress: Address,
    val payMethod: PayMethod,
    val orderLines: List<OrderLineInfo>,
    val coupon: Coupon?,
) {
    init {
        require(orderLines.isNotEmpty()) { "주문 상품이 최소 1개는 있어야 합니다." }
    }

    fun toOrder(): Order {
        val orderLines = orderLines.map { item ->
            OrderLine(
                productId = item.productId,
                quantity = item.quantity,
                unitPrice = item.unitPrice,
            )
        }

        return Order(
            userId = userId,
            deliveryAddress = deliveryAddress,
            payMethod = payMethod,
        ).apply {
            addOrderLines(orderLines)
        }
    }
}
