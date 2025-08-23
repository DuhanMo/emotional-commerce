package com.loopers.interfaces.api.order

import com.loopers.application.order.PlaceOrderOutput
import com.loopers.domain.order.Order
import com.loopers.domain.support.Money
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "주문 생성 응답")
data class PlaceOrderResponse(
    @field:Schema(description = "주문 식별자", example = "1")
    val orderId: Long,

    @field:Schema(description = "주문 번호", example = "20250824002114-a1b2c3")
    val orderNumber: String,

    @field:Schema(description = "주문 상태", example = "PENDING")
    val status: Order.OrderStatus,

    @field:Schema(description = "총 주문 금액", example = "25000")
    val totalAmount: Money,
) {
    companion object {
        fun from(output: PlaceOrderOutput): PlaceOrderResponse = PlaceOrderResponse(
            orderId = output.orderId,
            orderNumber = output.orderNumber,
            status = output.status,
            totalAmount = output.totalAmount,
        )
    }
}
