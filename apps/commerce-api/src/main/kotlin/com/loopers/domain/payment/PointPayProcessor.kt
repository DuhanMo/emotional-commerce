package com.loopers.domain.payment

import com.loopers.domain.order.Order
import com.loopers.domain.order.PayMethod
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
    override val support: PayMethod = PayMethod.POINT

    @Transactional
    override fun process(user: User, order: Order) {
        val point = pointRepository.getByUserIdWithLock(user.id)

        point.use(order.totalAmount)

        pointHistoryRepository.save(PointHistory.fromUse(user.id, point.id, order.totalAmount))
        pointRepository.save(point)
    }
}
