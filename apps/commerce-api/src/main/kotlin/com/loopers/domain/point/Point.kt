package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import com.loopers.domain.payment.Money
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "point")
@Entity
class Point(
    val userId: Long,
    var amount: Money = Money.ZERO,
) : BaseEntity() {
    fun charge(amount: Money) {
        this.amount += amount
    }

    fun use(amount: Money) {
        require(amount < this.amount) { "포인트가 부족합니다.(현재 포인트: ${this.amount}, 결제 금액: $amount)" }
        this.amount -= amount
    }
}
