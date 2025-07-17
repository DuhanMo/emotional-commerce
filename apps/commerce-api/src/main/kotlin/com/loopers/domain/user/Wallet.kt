package com.loopers.domain.user

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Table(name = "wallet")
@Entity
class Wallet(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn("user_id")
    val user: User,
    var point: Int = 0,
) : BaseEntity() {
    fun charge(point: Int) {
        require(point > 0) { "포인트는 0보다 커야 합니다." }
        this.point += point
    }
}
