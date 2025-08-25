package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import com.loopers.domain.support.Money
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "point")
@Entity
class Point(
    val userId: Long,
    var amount: Money = Money.ZERO,
    id: Long = 0L,
) : BaseEntity(id) {
    fun charge(amount: Money) {
        require(amount > Money.ZERO) { "포인트는 0보다 큰 금액으로 충전해야 합니다. (충전 금액: $amount)" }
        this.amount += amount
    }

    fun use(amount: Money) {
        require(amount < this.amount) { "포인트가 부족합니다.(현재 포인트: ${this.amount}, 결제 금액: $amount)" }
        this.amount -= amount
    }
}
