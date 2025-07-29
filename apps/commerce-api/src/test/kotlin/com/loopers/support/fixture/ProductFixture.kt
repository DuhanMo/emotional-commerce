package com.loopers.support.fixture

import com.loopers.domain.product.Product

fun createProduct(
    brandId: Long = 1L,
    name: String = "상품",
    description: String = "상품 설명",
    price: Int = 1_000,
    stock: Int = 100,
    imageUrl: String? = "www.image.url",
): Product = Product(
    brandId = brandId,
    name = name,
    description = description,
    price = price,
    stock = stock,
    imageUrl = imageUrl,
)