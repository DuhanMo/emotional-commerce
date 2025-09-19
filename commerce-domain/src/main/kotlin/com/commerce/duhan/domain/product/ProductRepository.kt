package com.commerce.duhan.domain.product

interface ProductRepository {
    fun getByProductId(id: Long): Product
}
