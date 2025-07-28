package com.loopers.infrastructure.point

import com.loopers.domain.point.PointLog
import org.springframework.data.jpa.repository.JpaRepository

interface PointLogJpaRepository: JpaRepository<PointLog, Long>