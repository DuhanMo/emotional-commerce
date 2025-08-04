package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "point_log")
@Entity
class PointLog private constructor(
    val userId: Long,
    val pointId: Long,
    val amount: Long,
) : BaseEntity() {
    companion object {
        fun fromUse(
            userId: Long,
            pointId: Long,
            amount: Long,
        ): PointLog = PointLog(userId, pointId, -amount)

        fun fromCharge(
            userId: Long,
            pointId: Long,
            amount: Long,
        ): PointLog = PointLog(userId, pointId, amount)
    }
}
