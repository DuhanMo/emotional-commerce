package com.loopers.domain.point

import org.springframework.stereotype.Component

@Component
class PointWriteService(
    private val pointRepository: PointRepository,
    private val pointLogRepository: PointLogRepository,
) {
    fun write(point: Point): Point = pointRepository.save(point)

    fun writeLog(pointLog: PointLog): PointLog = pointLogRepository.save(pointLog)
}
