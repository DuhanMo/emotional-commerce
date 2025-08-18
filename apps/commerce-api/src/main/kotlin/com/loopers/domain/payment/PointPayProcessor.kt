package com.loopers.domain.payment

import com.loopers.domain.order.Order
import com.loopers.domain.point.PointHistory
import com.loopers.domain.point.PointHistoryRepository
import com.loopers.domain.point.PointRepository
import com.loopers.domain.user.User
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PointPayProcessor(
    private val pointRepository: PointRepository,
    private val pointHistoryRepository: PointHistoryRepository,
) : PayProcessor {
    override val support: PaymentMethod = PaymentMethod.POINT

    @Transactional
    override fun process(command: PayProcessCommand) {
        val point = pointRepository.getByUserIdWithLock(command.userId)
        point.use(command.amount)

        pointRepository.save(point)
        pointHistoryRepository.save(PointHistory.fromUse(command.userId, point.id, command.amount))
    }
}

