package com.loopers.infrastructure.order

import com.loopers.domain.order.Order
import com.loopers.domain.order.OrderRepository
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(
    private val orderJpaRepository: OrderJpaRepository,
) : OrderRepository {
    override fun save(order: Order): Order = orderJpaRepository.save(order)

    override fun getById(orderId: Long): Order = orderJpaRepository.findByIdOrNull(orderId)
        ?: throw CoreException(ErrorType.NOT_FOUND, "존재하지 않는 주문입니다.")

    override fun findByUserId(userId: Long): List<Order> = orderJpaRepository.findByUserId(userId)
}
