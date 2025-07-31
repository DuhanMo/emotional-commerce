package com.loopers.domain.order

import com.loopers.domain.BaseEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.ZonedDateTime

@Table(name = "orders")
@Entity
class Order(
    val userId: Long,
    @Embedded
    val deliveryAddress: Address,
    orderLines: List<OrderLine> = emptyList(),
) : BaseEntity() {

    @Enumerated(EnumType.STRING)
    private var _status: OrderStatus = OrderStatus.PENDING

    val status: OrderStatus
        get() = _status

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    private val _orderLines: MutableList<OrderLine> = orderLines.toMutableList()

    val orderLines: List<OrderLine>
        get() = _orderLines.toList()

    val totalAmount: Int
        get() = _orderLines.sumOf { it.lineAmount }

    private var _orderedAt: ZonedDateTime = ZonedDateTime.now()

    val orderedAt: ZonedDateTime
        get() = _orderedAt

    init {
        require(orderLines.isNotEmpty()) { "주문 상품이 최소 1개는 있어야 합니다." }
    }

    fun addOrderLine(orderLine: OrderLine) {
        require(_status == OrderStatus.PENDING) { "결제 대기 상태에서만 주문 상품을 추가할 수 있습니다." }
        _orderLines.add(orderLine)
    }

    fun paid() {
        require(_status == OrderStatus.PENDING) { "결제 대기 상태에서만 결제 완료로 변경할 수 있습니다." }
        _status = OrderStatus.PAID
    }

    fun paymentFailed() {
        require(_status == OrderStatus.PENDING) { "결제 대기 상태에서만 결제 실패로 변경할 수 있습니다." }
        _status = OrderStatus.PAYMENT_FAILED
    }

    fun isPending(): Boolean = _status == OrderStatus.PENDING
    fun isPaid(): Boolean = _status == OrderStatus.PAID
    fun isPaymentFailed(): Boolean = _status == OrderStatus.PAYMENT_FAILED

    override fun guard() {
        require(_orderLines.isNotEmpty()) { "주문 상품이 최소 1개는 있어야 합니다." }
    }
}
