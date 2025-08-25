package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import com.loopers.domain.support.Money
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "point_history")
@Entity
class PointHistory(
    val userId: Long,
    val pointId: Long,
    @Enumerated(EnumType.STRING)
    val type: PointHistoryType,
    val amount: Money,
    id: Long = 0L,
) : BaseEntity(id) {
    companion object {
        fun fromUse(
            userId: Long,
            pointId: Long,
            amount: Money,
        ): PointHistory = PointHistory(userId, pointId, PointHistoryType.USE, amount)

        fun fromCharge(
            userId: Long,
            pointId: Long,
            amount: Money,
        ): PointHistory = PointHistory(userId, pointId, PointHistoryType.CHARGE, amount)
    }

    enum class PointHistoryType {
        USE,
        CHARGE,
    }
}
