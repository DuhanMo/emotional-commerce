package com.loopers.domain.product

import com.loopers.support.fixture.createInventory
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class InventoryTest : StringSpec({
    "재고를 초과하여 예약하면 예외가 발생한다" {
        val inventory = createInventory(availableQty = 3)

        shouldThrow<IllegalArgumentException> {
            inventory.reserve(5)
        }
    }

    "예약재고를 초과하여 재고를 확정하면 예외가 발생한다" {
        val inventory = createInventory(reservedQty = 4)

        shouldThrow<IllegalArgumentException> {
            inventory.commit(5)
        }
    }

    "재고를 예약하면 예약재고가 증가한다" {
        val inventory = createInventory(availableQty = 3)

        inventory.reserve(3)

        inventory.reservedQty shouldBe 3
    }

    "재고를 확정하면 예약재고가 감소하고 판매재고가 증가한다" {
        val inventory = createInventory(reservedQty = 2, soldQty = 0)

        inventory.commit(2)

        inventory.reservedQty shouldBe 0
        inventory.soldQty shouldBe 2
    }
})
