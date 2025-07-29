package com.loopers.application.point

import com.loopers.domain.point.ChargePointCommand
import com.loopers.domain.point.PointLog
import com.loopers.domain.point.PointReadService
import com.loopers.domain.point.PointWriteService
import com.loopers.domain.user.LoginId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointFacade(
    private val pointReadService: PointReadService,
    private val pointWriteService: PointWriteService,
) {
    @Transactional
    fun charge(command: ChargePointCommand): PointOutput {
        val point = pointReadService.getByUserLoginId(command.loginId)

        point.charge(command.point)

        pointWriteService.write(point)
        pointWriteService.writeLog(
            PointLog(
                userId = point.userId,
                pointId = point.id,
                amount = command.point
            )
        )

        return PointOutput.from(pointWriteService.write(point))
    }

    fun find(loginId: LoginId): PointOutput = PointOutput.from(pointReadService.getByUserLoginId(loginId))
}
