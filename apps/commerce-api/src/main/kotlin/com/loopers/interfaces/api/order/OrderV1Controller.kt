package com.loopers.interfaces.api.order

import com.loopers.application.order.OrderFacade
import com.loopers.domain.user.LoginId
import com.loopers.interfaces.api.ApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/orders")
class OrderV1Controller(
    private val orderFacade: OrderFacade,
) {
    @PostMapping
    fun placeOrder(
        @RequestHeader("X-USER-ID") loginId: String,
        @Valid @RequestBody request: PlaceOrderRequest,
    ): ApiResponse<PlaceOrderResponse> {
        val output = orderFacade.placeOrder(request.toInput(LoginId(loginId)))
        val response = PlaceOrderResponse.from(output)
        return ApiResponse.success(response)
    }
}
