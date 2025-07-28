package com.loopers.domain.point

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "point_log")
@Entity
class PointLog(
    val userId: Long,
    val pointId: Long,
    val amount: Int,
) : BaseEntity()
