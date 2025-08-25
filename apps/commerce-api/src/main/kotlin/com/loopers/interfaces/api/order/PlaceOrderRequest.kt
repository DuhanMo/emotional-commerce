package com.loopers.interfaces.api.order

import com.loopers.application.order.PlaceOrderInput
import com.loopers.domain.order.Address
import com.loopers.domain.support.Money
import com.loopers.domain.user.LoginId
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

@Schema(description = "주문 생성 요청")
data class PlaceOrderRequest(
    @field:Schema(description = "배송 주소", required = true)
    @field:Valid
    @field:NotNull(message = "배송 주소는 필수입니다.")
    val address: AddressRequest,

    @field:Schema(description = "결제 금액")
    @field:NotNull(message = "결제 금액은 필수입니다.")
    val totalAmount: Long?,

    @field:Schema(description = "주문 상품 목록", required = true)
    @field:Valid
    @field:Size(min = 1, message = "주문 상품은 최소 1개 이상이어야 합니다.")
    val orderItems: List<OrderLineRequest>,

    @field:Schema(description = "발급쿠폰 식별자", example = "1", required = true)
    val issuedCouponId: Long?,
) {
    @Schema(description = "배송 주소 정보")
    data class AddressRequest(
        @field:Schema(description = "도로명", example = "강남대로 123", required = true)
        @field:NotBlank(message = "도로명은 필수입니다.")
        val street: String,

        @field:Schema(description = "시/도", example = "서울시", required = true)
        @field:NotBlank(message = "시/도는 필수입니다.")
        val city: String,

        @field:Schema(description = "우편번호", example = "12345", required = true)
        @field:NotBlank(message = "우편번호는 필수입니다.")
        val zipCode: String,

        @field:Schema(description = "상세 주소", example = "101동 1001호")
        val detailAddress: String?,
    )

    @Schema(description = "주문 상품 정보")
    data class OrderLineRequest(
        @field:Schema(description = "상품 식별자", example = "1", required = true)
        @field:NotNull(message = "상품 ID는 필수입니다.")
        @field:Positive(message = "상품 ID는 양수여야 합니다.")
        val productId: Long?,

        @field:Schema(description = "상품 식별자", example = "1", required = true)
        @field:NotNull(message = "상품 ID는 필수입니다.")
        @field:Positive(message = "상품 ID는 양수여야 합니다.")
        val skuId: Long?,

        @field:Schema(description = "수량", example = "2", required = true)
        @field:NotNull(message = "수량은 필수입니다.")
        @field:Positive(message = "수량은 양수여야 합니다.")
        val quantity: Long?,

        @field:Schema(description = "단가", example = "10000", required = true)
        @field:NotNull(message = "단가는 필수입니다.")
        @field:Positive(message = "단가는 양수여야 합니다.")
        val unitPrice: Long?,
    )

    fun toInput(loginId: LoginId): PlaceOrderInput = PlaceOrderInput(
        loginId = loginId,
        orderItems = orderItems.map { item ->
            PlaceOrderInput.OrderLineInput(
                productId = item.productId!!,
                skuId = item.skuId!!,
                quantity = item.quantity!!,
                unitPrice = Money(item.unitPrice!!),
            )
        },
        deliveryAddress = Address(
            street = address.street,
            city = address.city,
            zipCode = address.zipCode,
            detailAddress = address.detailAddress,
        ),
        totalAmount = Money(totalAmount!!),
        issuedCouponId = issuedCouponId,
    )
}
