package com.loopers.domain.product

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ProductSummaryTest : StringSpec({
    "상품집계의 좋아요 수 감소 하여 음수가 될 수 없다" {
        val productSummary = ProductSummary(productId = 1L, likeCount = 0)

        shouldThrow<IllegalArgumentException> {
            productSummary.decreaseLikeCount()
        }
    }

    "상품집계의 좋아요 수 증가 하면 좋아요 수가 1 증가한다" {
        val productSummary = ProductSummary(productId = 1L, likeCount = 100)

        productSummary.increaseLikeCount()

        productSummary.likeCount shouldBe 101
    }

    "상품집계의 좋아요 수 감소 하면 좋아요 수가 1 감소한다" {
        val productSummary = ProductSummary(productId = 1L, likeCount = 100)

        productSummary.decreaseLikeCount()

        productSummary.likeCount shouldBe 99
    }
})
