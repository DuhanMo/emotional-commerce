package com.loopers.domain.payment

import com.loopers.domain.order.Order
import com.loopers.domain.order.OrderLine
import com.loopers.domain.order.OrderRepository
import com.loopers.domain.product.ProductRepository
import com.loopers.domain.user.User
import org.springframework.stereotype.Service

@Service
class PaymentService(
    payProcessors: List<PayProcessor>,
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
) {
    private val payProcessors = payProcessors.associateBy { it.support }

    fun pay(user: User, order: Order) {
        val payProcessor = payProcessors[order.payMethod]
            ?: throw IllegalArgumentException("지원하지 않는 결제 방법입니다: ${order.payMethod}")

        payProcessor.process(user, order)

        deductStock(order.orderLines)
        order.paid()

        orderRepository.save(order)
    }

    private fun deductStock(orderLines: List<OrderLine>) {
        val productIds = orderLines.map { it.productId }.distinct()
        val products = productRepository.findAllById(productIds).map { it.product }
            .associateBy { it.id }

        productRepository.findAllById(productIds)
        orderLines.forEach { orderLine ->
            val product = products[orderLine.productId]
                ?: throw IllegalArgumentException("존재하지 않는 상품: ${orderLine.productId}")

            product.deductStock(orderLine.quantity)
            productRepository.save(product)
        }
    }
}
