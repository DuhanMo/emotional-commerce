package com.loopers.domain.product

import com.loopers.domain.order.Order
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {
    @Transactional
    fun deductStock(order: Order) {
        order.orderLines.forEach { orderLine ->
            val product = productRepository.getByIdWithLock(orderLine.productId)

            productRepository.save(product)
        }
    }

    fun increaseLikeCount(productId: Long) {
        val product = productRepository.getById(productId)

        product.increaseLikeCount()

        productRepository.save(product)
    }

    fun decreaseLikeCount(productId: Long) {
        val product = productRepository.getById(productId)

        product.decreaseLikeCount()

        productRepository.save(product)
    }
}
