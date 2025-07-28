package com.loopers.application.point

import com.loopers.domain.point.Point
import com.loopers.domain.point.PointLog
import com.loopers.domain.point.PointLogRepository
import com.loopers.domain.point.PointRepository
import org.springframework.stereotype.Component

@Component
class PointWriter(
    private val pointRepository: PointRepository,
    private val pointLogRepository: PointLogRepository,
) {
    fun write(point: Point): Point = pointRepository.save(point)

    fun writeLog(pointLog: PointLog): PointLog = pointLogRepository.save(pointLog)
}
