package com.loopers.domain.product

import com.loopers.infrastructure.product.ProductJpaRepository
import com.loopers.support.fixture.createOrderLine
import com.loopers.support.fixture.createProduct
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull

class ProductServiceIGTest(
    private val productService: ProductService,
    private val productJpaRepository: ProductJpaRepository,
) : IntegrationSpec({
    Given("재고가 부족한 상품인 경우") {
        // 부족한 재고.
        val product = productJpaRepository.save(createProduct(stock = 1))
        val orderLines = listOf(
            createOrderLine(productId = product.id, unitPrice = 10_000L, quantity = 1),
            createOrderLine(productId = product.id, unitPrice = 10_000L, quantity = 1),
        )

        When("재고 감소를 시도하면") {
            Then("예외가 발생한다") {
                val exception = assertThrows<IllegalArgumentException> { productService.deductStock(orderLines) }
                exception.message shouldContain "재고가 부족합니다"
            }
        }
    }

    Given("재고가 충분한 상품인 경우") {
        // 충분한 재고.
        val product = productJpaRepository.save(createProduct(stock = 100))
        val orderLines = listOf(
            createOrderLine(productId = product.id, unitPrice = 10_000L, quantity = 1),
            createOrderLine(productId = product.id, unitPrice = 10_000L, quantity = 1),
        )

        When("재고 감소를 시도하면") {
            productService.deductStock(orderLines)

            Then("상품의 재고가 감소한다") {
                val foundProduct = productJpaRepository.findByIdOrNull(product.id)!!
                foundProduct.stock shouldBe 98
            }
        }
    }
})
