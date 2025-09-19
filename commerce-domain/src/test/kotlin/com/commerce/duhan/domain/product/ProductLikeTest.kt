package com.commerce.duhan.domain.product

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ProductLikeTest : StringSpec({
    "삭제된 상품 좋아요를 활성화 하면 상태가 활성화이다" {
        val productLike = ProductLike(
            userId = 1L,
            productId = 1L,
            status = ProductLikeStatus.DELETED,
        )

        productLike.active()

        productLike.status shouldBe ProductLikeStatus.ACTIVE
    }

    "활성화된 상품 좋아요를 삭제하면 상태가 삭제이다" {
        val productLike = ProductLike(
            userId = 1L,
            productId = 1L,
            status = ProductLikeStatus.ACTIVE,
        )

        productLike.delete()

        productLike.status shouldBe ProductLikeStatus.DELETED
    }
})
