package com.loopers.domain.product

data class ProductLikeCount(
    val productId: Long,
    val likeCount: Long,
) {
//    constructor(productId: Long?, likeCount: Long?) : this(
//        productId = productId!!,
//        likeCount = likeCount!!
//    )
}
