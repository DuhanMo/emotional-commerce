package com.loopers.application.order

import com.loopers.domain.order.OrderService
import com.loopers.domain.user.UserQueryService
import org.springframework.stereotype.Service

@Service
class OrderFacade(
    private val userQueryService: UserQueryService,
    private val orderService: OrderService,
) {
    fun placeOrder(input: PlaceOrderInput): PlaceOrderOutput {
        val user = userQueryService.getByLoginId(input.loginId)
        val order = orderService.createOrder(input.toCommand(user.id))
        return PlaceOrderOutput.from(order)
    }
}
