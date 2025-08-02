package com.loopers.application.point

import com.loopers.domain.point.ChargePointCommand
import com.loopers.domain.point.PointChargeService
import com.loopers.domain.point.PointQueryService
import com.loopers.domain.user.LoginId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PointFacade(
    private val pointChargeService: PointChargeService,
    private val pointQueryService: PointQueryService,
) {
    @Transactional
    fun charge(command: ChargePointCommand): PointOutput {
        val point = pointQueryService.getByUserLoginId(command.loginId)

        return PointOutput.from(pointChargeService.charge(point, command.point))
    }

    fun find(loginId: LoginId): PointOutput = PointOutput.from(pointQueryService.getByUserLoginId(loginId))
}
