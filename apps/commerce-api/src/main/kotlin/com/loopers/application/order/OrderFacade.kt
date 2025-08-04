package com.loopers.application.order

import com.loopers.domain.order.OrderService
import com.loopers.domain.payment.PaymentService
import com.loopers.domain.product.ProductService
import com.loopers.domain.user.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderFacade(
    private val userQueryService: UserQueryService,
    private val orderService: OrderService,
    private val paymentService: PaymentService,
    private val productService: ProductService,
) {
    @Transactional
    fun placeOrder(input: PlaceOrderInput): PlaceOrderOutput {
        val user = userQueryService.getByLoginId(input.loginId)
        val order = orderService.createOrder(input.toCommand(user.id))
        paymentService.pay(user, order)
        productService.deductStock(order)
        return PlaceOrderOutput.from(order)
    }
}
