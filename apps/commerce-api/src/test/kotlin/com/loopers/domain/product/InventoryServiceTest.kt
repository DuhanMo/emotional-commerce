package com.loopers.domain.product

import com.loopers.domain.order.OrderInfo
import com.loopers.domain.product.InventoryReservation.InventoryReservationStatus
import com.loopers.domain.support.Money
import com.loopers.support.fixture.createInventory
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class InventoryServiceTest : BehaviorSpec({
    val inventoryRepository = mockk<InventoryRepository>()
    val inventoryReservationRepository = mockk<InventoryReservationRepository>()
    
    val inventoryService = InventoryService(
        inventoryRepository,
        inventoryReservationRepository
    )

    Given("재고 예약 시") {
        val orderId = 1L
        val orderLines = listOf(
            OrderInfo.OrderLineInfo(
                productId = 1L,
                skuId = 1L,
                quantity = 2L,
                unitPrice = Money(1000)
            ),
            OrderInfo.OrderLineInfo(
                productId = 2L,
                skuId = 2L,
                quantity = 1L,
                unitPrice = Money(2000)
            )
        )
        
        val inventory1 = createInventory(skuId = 1L, availableQty = 10L)
        val inventory2 = createInventory(skuId = 2L, availableQty = 5L)

        When("충분한 재고가 있는 경우") {
            every { inventoryRepository.findAllBySkuIds(listOf(1L, 2L)) } returns listOf(inventory1, inventory2)
            every { inventoryRepository.saveAll(any<List<Inventory>>()) } returns listOf(inventory1, inventory2)
            every { inventoryReservationRepository.saveAll(any<List<InventoryReservation>>()) } returns emptyList()

            inventoryService.reserveAll(orderId, orderLines)

            Then("재고가 예약된다") {
                verify { inventoryRepository.saveAll(any<List<Inventory>>()) }
                verify { 
                    inventoryReservationRepository.saveAll(match { reservations ->
                        reservations.size == 2 &&
                        reservations.all { it.status == InventoryReservationStatus.RESERVED }
                    })
                }
            }
        }

        When("재고가 존재하지 않는 경우") {
            every { inventoryRepository.findAllBySkuIds(listOf(1L, 2L)) } returns listOf(inventory1)

            Then("예외가 발생한다") {
                shouldThrow<IllegalArgumentException> {
                    inventoryService.reserveAll(orderId, orderLines)
                }
            }
        }
    }

    Given("재고 확정 시") {
        val orderId = 1L
        val reservations = listOf(
            InventoryReservation(
                orderId = orderId,
                skuId = 1L,
                quantity = 2L,
                status = InventoryReservationStatus.RESERVED
            ),
            InventoryReservation(
                orderId = orderId,
                skuId = 2L,
                quantity = 1L,
                status = InventoryReservationStatus.RESERVED
            )
        )
        
        val inventory1 = createInventory(skuId = 1L, reservedQty = 2L)
        val inventory2 = createInventory(skuId = 2L, reservedQty = 1L)

        When("예약된 재고가 있는 경우") {
            every { inventoryReservationRepository.findAllByOrderId(orderId) } returns reservations
            every { inventoryRepository.findAllBySkuIds(listOf(1L, 2L)) } returns listOf(inventory1, inventory2)
            every { inventoryReservationRepository.saveAll(any<List<InventoryReservation>>()) } returns reservations
            every { inventoryRepository.saveAll(any<List<Inventory>>()) } returns listOf(inventory1, inventory2)

            inventoryService.commitAll(orderId)

            Then("재고가 확정된다") {
                verify { 
                    inventoryReservationRepository.saveAll(match { it ->
                        it.all { reservation -> reservation.status == InventoryReservationStatus.COMMITTED }
                    })
                }
                verify { inventoryRepository.saveAll(any<List<Inventory>>()) }
            }
        }
    }

    Given("재고 해제 시") {
        val orderId = 1L
        val reservations = listOf(
            InventoryReservation(
                orderId = orderId,
                skuId = 1L,
                quantity = 2L,
                status = InventoryReservationStatus.RESERVED
            )
        )
        
        val inventory = createInventory(skuId = 1L, reservedQty = 2L)

        When("예약된 재고가 있는 경우") {
            every { inventoryReservationRepository.findAllByOrderId(orderId) } returns reservations
            every { inventoryRepository.findAllBySkuIds(listOf(1L)) } returns listOf(inventory)
            every { inventoryReservationRepository.saveAll(any<List<InventoryReservation>>()) } returns reservations
            every { inventoryRepository.saveAll(any<List<Inventory>>()) } returns listOf(inventory)

            inventoryService.releaseAll(orderId)

            Then("재고가 해제된다") {
                verify { 
                    inventoryReservationRepository.saveAll(match { it ->
                        it.all { reservation -> reservation.status == InventoryReservationStatus.RELEASED }
                    })
                }
                verify { inventoryRepository.saveAll(any<List<Inventory>>()) }
            }
        }
    }

    afterTest {
        clearAllMocks()
    }
})