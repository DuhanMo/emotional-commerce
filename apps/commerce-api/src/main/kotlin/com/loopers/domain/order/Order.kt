package com.loopers.domain.order

import com.loopers.domain.BaseEntity
import com.loopers.domain.support.Money
import com.loopers.domain.support.sumOfMoney
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
    @Embedded
    val deliveryAddress: Address,
    val totalAmount: Money,
    val orderNumber: String = OrderNumberGenerator.generate(),
) : BaseEntity() {
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderLines: MutableList<OrderLine> = mutableListOf()

    var couponId: Long? = null

    val originAmount: Money
            get() = orderLines.map { it.lineAmount }.sumOfMoney()

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

    fun payFail() {
        require(status == OrderStatus.PENDING) { "결제 대기 상태에서만 결제 실패로 변경할 수 있습니다." }
        status = OrderStatus.PAY_FAILED
    }

    enum class OrderStatus {
        PENDING, // 초기 상태 (주문 생성)
        PAID, // 결제 완료
        PAY_FAILED, // 결제 실패
    }
}
