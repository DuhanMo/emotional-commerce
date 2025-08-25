package com.loopers.support.fixture

import com.loopers.domain.product.Inventory
import com.loopers.domain.product.Product
import com.loopers.domain.product.Product.ProductStatus
import com.loopers.domain.support.Money

fun createProduct(
    brandId: Long = 1L,
    name: String = "테스트 상품",
    description: String = "테스트 상품 설명",
    status: ProductStatus = ProductStatus.ACTIVE,
    price: Money = Money(10_000),
    imageUrl: String? = "/test/image.png",
    likeCount: Long = 0,
    id: Long = 0L,
): Product = Product(
    brandId = brandId,
    name = name,
    description = description,
    status = status,
    price = price,
    likeCount = likeCount,
    imageUrl = imageUrl,
    id = id,
)

fun createInventory(
    productId: Long = 1L,
    skuId: Long = 1L,
    availableQty: Long = 100L,
    reservedQty: Long = 0L,
    soldQty: Long = 0L,
    id: Long = 0L,
): Inventory = Inventory(
    skuId = skuId,
    availableQty = availableQty,
    reservedQty = reservedQty,
    soldQty = soldQty,
    id = id,
)
