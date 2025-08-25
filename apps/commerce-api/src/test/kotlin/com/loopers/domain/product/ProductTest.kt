package com.loopers.domain.product

import com.loopers.support.fixture.createProduct
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ProductTest : StringSpec({
    "상품 좋아요 수는 음수로 차감할 수 없다" {
        val product = createProduct(likeCount = 0)
        shouldThrow<IllegalStateException> {
            product.decreaseLikeCount()
        }
    }

    "상품 좋아요 수 증가시 상품 좋아요 수가 증가한다" {
        val product = createProduct(likeCount = 0)

        product.increaseLikeCount()

        product.likeCount shouldBe 1
    }
})
