package com.commerce.duhan.db.point

import com.commerce.duhan.domain.point.PointHistory
import org.springframework.data.jpa.repository.JpaRepository

interface PointHistoryJpaRepository : JpaRepository<PointHistory, Long>
