package com.loopers.domain.product

import com.loopers.infrastructure.product.ProductJpaRepository
import com.loopers.support.fixture.createOrder
import com.loopers.support.fixture.createOrderLine
import com.loopers.support.fixture.createProduct
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull

class ProductServiceIGTest(
    private val productService: ProductService,
    private val productJpaRepository: ProductJpaRepository,
) : IntegrationSpec({
    Given("재고가 부족한 상품인 경우") {
        // 부족한 재고.
        val product = productJpaRepository.save(createProduct(stock = 1))
        val order = createOrder().apply {
            addOrderLines(
                listOf(
                    createOrderLine(productId = product.id, unitPrice = 10_000L, quantity = 1),
                    createOrderLine(productId = product.id, unitPrice = 10_000L, quantity = 1),
                ),
            )
        }

        When("재고 감소를 시도하면") {
            Then("예외가 발생한다") {
                val exception = assertThrows<IllegalArgumentException> { productService.deductStock(order) }
                exception.message shouldContain "재고가 부족합니다"
            }
        }
    }

    Given("재고가 충분한 상품인 경우") {
        // 충분한 재고.
        val product = productJpaRepository.save(createProduct(stock = 100))
        val order = createOrder().apply {
            addOrderLines(
                listOf(
                    createOrderLine(productId = product.id, unitPrice = 10_000L, quantity = 1),
                    createOrderLine(productId = product.id, unitPrice = 10_000L, quantity = 1),
                ),
            )
        }

        When("재고 감소를 시도하면") {
            productService.deductStock(order)

            Then("상품의 재고가 감소한다") {
                val foundProduct = productJpaRepository.findByIdOrNull(product.id)!!
                foundProduct.stock shouldBe 98
            }
        }
    }

    Given("재고보다 많은 요청이 동시에 재고 차감을 하는 경우 - 스레드 버전") {
        val initialStock = 10
        val numberOfThreads = 15

        val product = productJpaRepository.save(createProduct(stock = initialStock))

        val order = createOrder().apply {
            addOrderLines(listOf(createOrderLine(productId = product.id, quantity = 1)))
        }
        val latch = CountDownLatch(numberOfThreads)
        val executor = Executors.newFixedThreadPool(numberOfThreads)
        val results = ConcurrentLinkedQueue<Result<Unit>>()

        When("동시에 재고 감소를 시도하면") {
            repeat(numberOfThreads) { threadIndex ->
                executor.submit {
                    try {
                        val result = runCatching {
                            productService.deductStock(order)
                        }
                        results.add(result)
                    } finally {
                        latch.countDown()
                    }
                }
            }
            latch.await(30, TimeUnit.SECONDS)
            executor.shutdown()

            Then("재고는 0이 되고 재고 보다 많은 요청은 실패한다") {
                val stock = productJpaRepository.findByIdOrNull(product.id)!!.stock
                stock shouldBe 0
                results.count { it.isSuccess } shouldBe initialStock
                results.count { it.isFailure } shouldBe numberOfThreads - initialStock
            }
        }
    }

    Given("재고보다 많은 요청이 동시에 재고 차감을 하는 경우 - 코루틴 버전") {
        val initialStock = 10
        val numberOfRequest = 15

        val product = productJpaRepository.save(createProduct(stock = initialStock))

        val order = createOrder().apply {
            addOrderLines(listOf(createOrderLine(productId = product.id, quantity = 1)))
        }

        When("동시에 재고 감소를 시도하면") {
            val results = (0 until numberOfRequest).map {
                async(Dispatchers.IO) {
                    runCatching { productService.deductStock(order) }
                }
            }.awaitAll()

            Then("재고는 0이 되고 재고 보다 많은 요청은 실패한다") {
                val stock = productJpaRepository.findByIdOrNull(product.id)!!.stock
                stock shouldBe 0
                results.count { it.isSuccess } shouldBe initialStock
                results.count { it.isFailure } shouldBe numberOfRequest - initialStock
            }
        }
    }

    Given("재고보다 적은 요청이 동시에 재고 차감을 하는 경우 - 코루틴 버전") {
        val initialStock = 10
        val numberOfRequest = 4

        val product = productJpaRepository.save(createProduct(stock = initialStock))

        val order = createOrder().apply {
            addOrderLines(listOf(createOrderLine(productId = product.id, quantity = 1)))
        }

        When("동시에 재고 감소를 시도하면") {
            val results = (0 until numberOfRequest).map {
                async(Dispatchers.IO) {
                    runCatching {
                        productService.deductStock(order)
                    }
                }
            }.awaitAll()

            Then("재고는 요청수만큼 감소하고 모든 요청은 성공한다") {
                val stock = productJpaRepository.findByIdOrNull(product.id)!!.stock
                stock shouldBe initialStock - numberOfRequest
                results.count { it.isSuccess } shouldBe numberOfRequest
                results.count { it.isFailure } shouldBe 0
            }
        }
    }
})
