package com.loopers.domain.order

import com.loopers.domain.product.ProductRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class OrderService(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
) {
    @Transactional
    fun createOrder(command: CreateOrderCommand): Order {
        validateProductStock(command)

        val order = command.toOrder()
        return orderRepository.save(order)
    }

    private fun validateProductStock(command: CreateOrderCommand) {
        val orderItems = command.orderItems
        val productIds = orderItems.map { it.productId }.distinct()

        val products = productRepository.findAllById(productIds)
            .map { it.product }
            .associateBy { it.id }

        orderItems.forEach { orderItem ->
            val product = products[orderItem.productId]
            requireNotNull(product)
            check(product.stock >= orderItem.quantity) { "상품 재고가 부족합니다: ${orderItem.productId}" }
        }
    }
}
