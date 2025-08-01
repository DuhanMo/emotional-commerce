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
    fun createOrder(command: CreateOrderCommand): OrderInfo {
        validateProductStock(command)

        val order = command.toOrder()

        return OrderInfo.from(orderRepository.save(order))
    }

    private fun validateProductStock(command: CreateOrderCommand) {
        val orderLineInfos = command.orderLines
        val productIds = orderLineInfos.map { it.productId }.distinct()

        val products = productRepository.findAllById(productIds)
            .map { it.product }
            .associateBy { it.id }

        orderLineInfos.forEach { orderItem ->
            val product = products[orderItem.productId]
            requireNotNull(product)
            check(product.stock >= orderItem.quantity) { "상품 재고가 부족합니다: ${orderItem.productId}" }
        }
    }
}

data class OrderInfo(
    val id: Long,
    val userId: Long,
    val deliveryAddress: Address,
    val payMethod: PayMethod,
    val status: OrderStatus,
    val totalAmount: Long,
    val orderLineInfos: List<OrderLineInfo>,
) {
    data class OrderLineInfo(
        val productId: Long,
        val quantity: Int,
        val unitPrice: Long,
    ) {
        val lineAmount: Long get() = quantity * unitPrice
    }

    companion object {
        fun from(order: Order): OrderInfo {
            return OrderInfo(
                id = order.id,
                userId = order.userId,
                deliveryAddress = order.deliveryAddress,
                payMethod = order.payMethod,
                status = order.status,
                totalAmount = order.totalAmount,
                orderLineInfos = order.orderLines.map { orderLine ->
                    OrderLineInfo(
                        productId = orderLine.productId,
                        quantity = orderLine.quantity,
                        unitPrice = orderLine.unitPrice,
                    )
                },
            )
        }
    }
}
