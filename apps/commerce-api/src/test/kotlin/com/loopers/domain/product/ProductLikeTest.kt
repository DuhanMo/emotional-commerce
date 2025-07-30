package com.loopers.domain.product

import com.loopers.domain.product.ProductLikeStatus.ACTIVE
import com.loopers.domain.product.ProductLikeStatus.DELETED
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ProductLikeTest : StringSpec({
    "좋아요 최초 생성하면 상태는 ACTIVE 이고 deletedAt 이 null 이다" {
        val productLike = ProductLike(productId = 1L, userId = 1L)

        productLike.status shouldBe ACTIVE
        productLike.deletedAt shouldBe null
    }

    "좋아요를 취소하면 상태는 DELETED 이고 deletedAt 이 not null 이다" {
        val productLike = ProductLike(productId = 1L, userId = 1L)

        productLike.unlike()

        productLike.status shouldBe DELETED
        productLike.deletedAt shouldNotBe null
    }

    "취소된 좋아요를 다시 좋아요 등록하면 상태는 ACTIVE 이고 deletedAt 이 null 이다" {
        val productLike = ProductLike(productId = 1L, userId = 1L)

        productLike.unlike()
        productLike.deletedAt shouldNotBe null

        productLike.like()
        productLike.status shouldBe ACTIVE
        productLike.deletedAt shouldBe null
    }

    "좋아요를 두 번 등록해도 상태는 ACTIVE 이고 deletedAt 이 null 이다" {
        val productLike = ProductLike(productId = 1L, userId = 1L)

        productLike.like()
        productLike.like()

        productLike.status shouldBe ACTIVE
        productLike.deletedAt shouldBe null
    }

    "좋아요를 두 번 취소해도 상태는 DELETED 이고 deletedAt 이 not null 이다" {
        val productLike = ProductLike(productId = 1L, userId = 1L)

        productLike.unlike()
        productLike.unlike()

        productLike.status shouldBe DELETED
        productLike.deletedAt shouldNotBe null
    }
})
