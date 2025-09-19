package com.commerce.duhan.domain.fixtures

import com.commerce.duhan.domain.product.Product
import com.commerce.duhan.domain.product.ProductLike
import com.commerce.duhan.domain.product.ProductLikeStatus
import com.commerce.duhan.domain.product.ProductStatus

fun createProduct(
    brandId: Long = 1L,
    name: String = "테스트 상품",
    description: String = "테스트 상품 설명",
    status: ProductStatus = ProductStatus.ON_SALE,
    likeCount: Long = 0L,
): Product = Product(
    brandId = brandId,
    name = name,
    description = description,
    status = status,
    likeCount = likeCount,
)

fun createProductLike(
    userId: Long = 1L,
    productId: Long = 1L,
    status: ProductLikeStatus = ProductLikeStatus.ACTIVE,
): ProductLike = ProductLike(
    userId = userId,
    productId = productId,
    status = status,
)
