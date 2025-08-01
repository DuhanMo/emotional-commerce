package com.loopers.domain.order

import com.loopers.domain.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Table(name = "orders")
@Entity
class Order(
    val userId: Long,
    @Embedded
    val deliveryAddress: Address,
    val payMethod: PayMethod,
    @Enumerated(EnumType.STRING)
    var status: OrderStatus = OrderStatus.PENDING,
) : BaseEntity() {
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderLines: MutableList<OrderLine> = mutableListOf()

    val totalAmount: Long
        get() = orderLines.sumOf { it.lineAmount }

    fun addOrderLines(orderLines: List<OrderLine>) {
        require(orderLines.count() > 0) { "주문 상품이 최소 1개는 있어야 합니다." }
        val orderLines = orderLines.map { it.setOrder(this) }
        this.orderLines.addAll(orderLines)
    }

    fun paid() {
        require(status == OrderStatus.PENDING) { "결제 대기 상태에서만 결제 완료로 변경할 수 있습니다." }
        status = OrderStatus.PAID
    }

    fun paymentFailed() {
        require(status == OrderStatus.PENDING) { "결제 대기 상태에서만 결제 실패로 변경할 수 있습니다." }
        status = OrderStatus.PAYMENT_FAILED
    }

    fun isPending(): Boolean = status == OrderStatus.PENDING
    fun isPaid(): Boolean = status == OrderStatus.PAID
    fun isPaymentFailed(): Boolean = status == OrderStatus.PAYMENT_FAILED
}
