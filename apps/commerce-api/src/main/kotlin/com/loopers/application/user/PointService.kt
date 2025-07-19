package com.loopers.application.user

import com.loopers.domain.user.ChargePointCommand
import com.loopers.domain.user.LoginId
import com.loopers.domain.user.PointReader
import com.loopers.domain.user.PointWriter
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

        return PointOutput.from(pointWriter.write(point))
    }

    fun find(loginId: LoginId): PointOutput = PointOutput.from(pointReader.getByLoginId(loginId))
}
