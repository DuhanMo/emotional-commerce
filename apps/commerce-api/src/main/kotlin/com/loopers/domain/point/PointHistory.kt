package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import com.loopers.domain.payment.Money
import com.loopers.domain.point.PointHistoryType.CHARGE
import com.loopers.domain.point.PointHistoryType.USE
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "point_history")
@Entity
class PointHistory private constructor(
    val userId: Long,
    val pointId: Long,
    @Enumerated(EnumType.STRING)
    val type: PointHistoryType,
    val amount: Money,
) : BaseEntity() {
    companion object {
        fun fromUse(
            userId: Long,
            pointId: Long,
            amount: Money,
        ): PointHistory = PointHistory(userId, pointId, USE, amount)

        fun fromCharge(
            userId: Long,
            pointId: Long,
            amount: Money,
        ): PointHistory = PointHistory(userId, pointId, CHARGE, amount)
    }
}
