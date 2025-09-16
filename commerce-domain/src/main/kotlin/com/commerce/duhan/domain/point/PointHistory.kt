package com.commerce.duhan.domain.point

import com.commerce.duhan.domain.common.BaseEntity
import com.commerce.duhan.domain.common.Money
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
