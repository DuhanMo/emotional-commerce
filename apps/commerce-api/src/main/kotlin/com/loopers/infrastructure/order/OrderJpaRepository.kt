package com.loopers.infrastructure.order

import com.loopers.domain.order.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface OrderJpaRepository : JpaRepository<Order, Long> {
    fun findByUserId(userId: Long): List<Order>

    @Query("SELECT o FROM Order o JOIN FETCH o.orderLines WHERE o.id = :id")
    fun findByIdWithOrderLines(@Param("id") id: Long): Order?
}
