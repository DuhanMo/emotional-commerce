package com.loopers.domain.order

import org.springframework.stereotype.Service

@Service
class OrderQueryService(
    private val orderRepository: OrderRepository,
) {
    fun getById(id: Long): Order = orderRepository.getById(id)
}
