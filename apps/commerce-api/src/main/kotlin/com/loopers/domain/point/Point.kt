package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "point")
@Entity
class Point(
    val userId: Long,
    var amount: Long = 0L,
) : BaseEntity() {
    fun charge(amount: Long) {
        require(amount > 0) { "포인트는 0보다 커야 합니다." }
        this.amount += amount
    }

    fun use(amount: Long) {
        require(amount < this.amount) { "포인트가 부족합니다.(현재 포인트: ${this.amount}, 결제 금액: $amount)" }
        this.amount -= amount
    }
}
