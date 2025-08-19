 package com.loopers.domain.product

 import com.loopers.domain.product.ProductLike.ProductLikeStatus
 import io.kotest.core.spec.style.StringSpec
 import io.kotest.matchers.shouldBe
 import io.kotest.matchers.shouldNotBe

 class ProductLikeTest : StringSpec({
    "싱픔 좋아요 최초 생성하면 상태는 ACTIVE 이고 deletedAt 이 null 이다" {
        val productLike = ProductLike(productId = 1L, userId = 1L)

        productLike.status shouldBe ProductLikeStatus.ACTIVE
        productLike.deletedAt shouldBe null
    }

    "상품 좋아요를 좋아요 취소하면 상태는 DELETED 이고 deletedAt 이 not null 이다" {
        val productLike = ProductLike(productId = 1L, userId = 1L)

        productLike.unlike()

        productLike.status shouldBe ProductLikeStatus.DELETED
        productLike.deletedAt shouldNotBe null
    }

    "취소된 상품 좋아요를 다시 좋아요 등록하면 상태는 ACTIVE 이고 deletedAt 이 null 이다" {
        val productLike = ProductLike(productId = 1L, userId = 1L)

        productLike.unlike()
        productLike.deletedAt shouldNotBe null

        productLike.like()
        productLike.status shouldBe ProductLikeStatus.ACTIVE
        productLike.deletedAt shouldBe null
    }

    "상품 좋아요를 좋아요 등록을 두 번해도 상태는 ACTIVE 이고 deletedAt 이 null 이다" {
        val productLike = ProductLike(productId = 1L, userId = 1L)

        repeat(2) { productLike.like() }

        productLike.status shouldBe ProductLikeStatus.ACTIVE
        productLike.deletedAt shouldBe null
    }

    "상품 좋아요를 좋아요 취소를 두 번 해도 상태는 DELETED 이고 deletedAt 이 not null 이다" {
        val productLike = ProductLike(productId = 1L, userId = 1L)

        repeat(2) { productLike.unlike() }

        productLike.status shouldBe ProductLikeStatus.DELETED
        productLike.deletedAt shouldNotBe null
    }
 })
