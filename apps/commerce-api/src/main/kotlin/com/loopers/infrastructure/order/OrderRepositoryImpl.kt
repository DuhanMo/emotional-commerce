package com.loopers.infrastructure.order

import com.loopers.domain.order.Order
import com.loopers.domain.order.OrderRepository
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(
    private val jpaRepository: OrderJpaRepository,
) : OrderRepository {
    override fun save(order: Order): Order = jpaRepository.save(order)

    override fun getById(id: Long): Order = jpaRepository.findByIdOrNull(id)
        ?: throw CoreException(ErrorType.NOT_FOUND, "존재하지 않는 주문입니다.(orderId: $id)")

    override fun findByUserId(userId: Long): List<Order> = jpaRepository.findByUserId(userId)

    override fun getByOrderNumber(orderNumber: String): Order = jpaRepository.findByOrderNumber(orderNumber)
        ?: throw CoreException(ErrorType.NOT_FOUND, "존재하지 않는 주문입니다.(orderNumber: $orderNumber)")
}
