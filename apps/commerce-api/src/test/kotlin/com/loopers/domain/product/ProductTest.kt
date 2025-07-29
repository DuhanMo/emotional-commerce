package com.loopers.domain.product

import com.loopers.support.fixture.createProduct
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ProductTest : StringSpec({
    "재고를 초과하여 재고 차감하면 예외가 발생한다" {
        val product = createProduct(stock = 1)

        shouldThrow<IllegalArgumentException> {
            product.deductStock(2)
        }
    }

    "재고와 동일하게 재고 차감할 수 있다" {
        val product = createProduct(stock = 2)

        product.deductStock(2)

        product.stock shouldBe 0
    }
})