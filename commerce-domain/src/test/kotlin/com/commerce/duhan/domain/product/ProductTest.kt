package com.commerce.duhan.domain.product

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ProductTest : StringSpec({
    "이미 삭제된 경우 판매를 할 수 없다" {
        val product = Product(
            brandId = 1L,
            name = "테스트 상품",
            description = "테스트 상품 설명",
            status = ProductStatus.DELETED,
        )

        shouldThrow<IllegalArgumentException> {
            product.onSale()
        }
    }

    "이미 삭제된 경우 판매중지를 할 수 없다" {
        val product = Product(
            brandId = 1L,
            name = "테스트 상품",
            description = "테스트 상품 설명",
            status = ProductStatus.DELETED,
        )

        shouldThrow<IllegalArgumentException> {
            product.stopSale()
        }
    }

    "삭제 시 삭제일시가 기록된다" {
        val product = Product(
            brandId = 1L,
            name = "테스트 상품",
            description = "테스트 상품 설명",
            status = ProductStatus.ON_SALE,
        )

        product.delete()

        product.deletedAt shouldNotBe null
    }

    "좋아요 추가 하는 경우 좋아요 수 증가한다" {
        val product = Product(
            brandId = 1L,
            name = "테스트 상품",
            description = "테스트 상품 설명",
            status = ProductStatus.ON_SALE,
            likeCount = 2,
        )

        product.addLike()

        product.likeCount shouldBe 3
    }

    "좋아요 제거 하는 경우 좋아요 수 감소한다" {
        val product = Product(
            brandId = 1L,
            name = "테스트 상품",
            description = "테스트 상품 설명",
            status = ProductStatus.ON_SALE,
            likeCount = 2,
        )

        product.removeLike()

        product.likeCount shouldBe 1
    }

    "좋아요 제거 하는 경우 좋아요 수는 음수로 감소하지 않는다" {
        val product = Product(
            brandId = 1L,
            name = "테스트 상품",
            description = "테스트 상품 설명",
            status = ProductStatus.ON_SALE,
            likeCount = 1,
        )

        repeat(5) {
            product.removeLike()
        }

        product.likeCount shouldBeGreaterThanOrEqual 0
    }

    "삭제된 상품은 좋아요 추가할 수 없다" {
        val product = Product(
            brandId = 1L,
            name = "테스트 상품",
            description = "테스트 상품 설명",
            status = ProductStatus.DELETED,
        )

        shouldThrow<IllegalArgumentException> {
            product.addLike()
        }
    }

    "삭제된 상품은 좋아요 제거할 수 없다" {
        val product = Product(
            brandId = 1L,
            name = "테스트 상품",
            description = "테스트 상품 설명",
            status = ProductStatus.DELETED,
        )

        shouldThrow<IllegalArgumentException> {
            product.removeLike()
        }
    }
})
