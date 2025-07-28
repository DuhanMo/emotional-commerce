package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "point")
@Entity
class Point(
    val userId: Long,
    var amount: Int = 0,
) : BaseEntity() {
    fun charge(amount: Int) {
        require(amount > 0) { "포인트는 0보다 커야 합니다." }
        this.amount += amount
    }
}
