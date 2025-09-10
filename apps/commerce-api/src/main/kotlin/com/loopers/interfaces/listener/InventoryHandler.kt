package com.loopers.interfaces.listener

import com.loopers.domain.product.InventoryService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class InventoryHandler(
    private val inventoryService: InventoryService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

}
