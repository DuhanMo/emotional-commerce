package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import com.loopers.domain.user.User
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Table(name = "point")
@Entity
class Point(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn("user_id")
    val user: User,
    var amount: Int = 0,
) : BaseEntity() {
    fun charge(amount: Int) {
        require(amount > 0) { "포인트는 0보다 커야 합니다." }
        this.amount += amount
    }
}
