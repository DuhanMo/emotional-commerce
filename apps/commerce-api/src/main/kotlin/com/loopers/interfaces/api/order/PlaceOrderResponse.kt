package com.loopers.interfaces.api.order

import com.loopers.application.order.PlaceOrderOutput
import com.loopers.domain.order.OrderStatus
import com.loopers.domain.order.PayMethod
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "주문 생성 응답")
data class PlaceOrderResponse(
    @Schema(description = "주문 식별자", example = "1")
    val id: Long,

    @Schema(description = "사용자 식별자", example = "1")
    val userId: Long,

    @Schema(description = "배송 주소")
    val deliveryAddress: AddressResponse,

    @Schema(description = "결제 방법", example = "POINT")
    val payMethod: PayMethod,

    @Schema(description = "주문 상태", example = "PENDING")
    val status: OrderStatus,

    @Schema(description = "총 주문 금액", example = "25000")
    val totalAmount: Long,

    @Schema(description = "주문 상품 목록")
    val orderLines: List<OrderLineResponse>,
) {
    @Schema(description = "배송 주소 정보")
    data class AddressResponse(
        @Schema(description = "도로명", example = "강남대로 123")
        val street: String,

        @Schema(description = "시/도", example = "서울시")
        val city: String,

        @Schema(description = "우편번호", example = "12345")
        val zipCode: String,

        @Schema(description = "상세 주소", example = "101동 1001호")
        val detailAddress: String?,
    )

    @Schema(description = "주문 상품 정보")
    data class OrderLineResponse(
        @Schema(description = "상품 식별자", example = "1")
        val productId: Long,

        @Schema(description = "수량", example = "2")
        val quantity: Int,

        @Schema(description = "단가", example = "10000")
        val unitPrice: Long,
    )

    companion object {
        fun from(output: PlaceOrderOutput): PlaceOrderResponse = PlaceOrderResponse(
            id = output.id,
            userId = output.userId,
            deliveryAddress = AddressResponse(
                street = output.deliveryAddress.street,
                city = output.deliveryAddress.city,
                zipCode = output.deliveryAddress.zipCode,
                detailAddress = output.deliveryAddress.detailAddress,
            ),
            payMethod = output.payMethod,
            status = output.status,
            totalAmount = output.totalAmount,
            orderLines = output.orderLineInfos.map { orderLine ->
                OrderLineResponse(
                    productId = orderLine.productId,
                    quantity = orderLine.quantity,
                    unitPrice = orderLine.unitPrice,
                )
            },
        )
    }
}
