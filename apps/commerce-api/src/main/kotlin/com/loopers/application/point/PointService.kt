package com.loopers.application.point

import com.loopers.domain.point.ChargePointCommand
import com.loopers.domain.point.PointLog
import com.loopers.application.point.PointReader
import com.loopers.application.point.PointWriter
import com.loopers.domain.user.LoginId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointService(
    private val pointReader: PointReader,
    private val pointWriter: PointWriter,
) {
    @Transactional
    fun charge(command: ChargePointCommand): PointOutput {
        val point = pointReader.getByLoginId(command.loginId)

        point.charge(command.point)

        pointWriter.write(point)
        pointWriter.writeLog(PointLog(userId = point.userId, pointId = point.id, amount = command.point))

        return PointOutput.from(pointWriter.write(point))
    }

    fun find(loginId: LoginId): PointOutput = PointOutput.from(pointReader.getByLoginId(loginId))
}
