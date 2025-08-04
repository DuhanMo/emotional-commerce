package com.loopers.domain.product

import com.loopers.domain.order.OrderLine
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {
    @Transactional
    fun deductStock(orderLines: List<OrderLine>) {
        val productIds = orderLines.map { it.productId }
        val products = productRepository.findAllById(productIds).associateBy { it.id }
        orderLines.forEach { orderLine ->
            val product = products[orderLine.productId]
                ?: throw IllegalArgumentException("존재하지 않는 상품: ${orderLine.productId}")
            product.deductStock(orderLine.quantity)

            productRepository.save(product)
        }
    }
}
