package com.loopers.domain.order

import com.loopers.domain.BaseEntity
import com.loopers.domain.support.Money
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
    @Enumerated(EnumType.STRING)
    var status: OrderStatus = OrderStatus.PENDING,
    val totalAmount: Money,
) : BaseEntity() {
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderLines: MutableList<OrderLine> = mutableListOf()

    @Embedded
    val deliveryAddress: Address? = null

    fun addOrderLines(orderItems: List<OrderInfo.OrderLineInfo>) {
        require(orderItems.count() > 0) { "주문 상품이 최소 1개는 있어야 합니다." }
        orderLines.addAll(
            orderItems.map {
                OrderLine(
                    productId = it.productId,
                    skuId = it.skuId,
                    quantity = it.quantity,
                    unitPrice = it.unitPrice,
                    order = this,
                )
            },
        )
    }

    fun paid() {
        require(status == OrderStatus.PENDING) { "결제 대기 상태에서만 결제 완료로 변경할 수 있습니다." }
        status = OrderStatus.PAID
    }

    fun paymentFailed() {
        require(status == OrderStatus.PENDING) { "결제 대기 상태에서만 결제 실패로 변경할 수 있습니다." }
        status = OrderStatus.FAILED
    }

    fun isPending(): Boolean = status == OrderStatus.PENDING
    fun isPaid(): Boolean = status == OrderStatus.PAID
    fun isPaymentFailed(): Boolean = status == OrderStatus.FAILED

    enum class OrderStatus {
        PENDING, // 초기 상태 (주문 생성)
        PAID, // 결제 완료
        FAILED, // 결제 실패
        CANCELLED, // 주문 취소
    }
}
