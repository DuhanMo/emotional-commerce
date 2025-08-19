package com.loopers.domain.product

import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {
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
