package com.loopers.support.fixture

import com.loopers.domain.product.Product

fun createProduct(
    brandId: Long = 1L,
    name: String = "테스트 상품",
    description: String = "테스트 상품 설명",
    price: Int = 10_000,
    stock: Int = 100,
    imageUrl: String? = "/test/image.png",
    id: Long = 0L,
): Product = Product(
    brandId = brandId,
    name = name,
    description = description,
    price = price,
    stock = stock,
    imageUrl = imageUrl,
    id = id,
)
