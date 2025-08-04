package com.loopers.domain.payment

import com.loopers.domain.order.Order
import com.loopers.domain.order.PayMethod
import com.loopers.domain.point.PointLog
import com.loopers.domain.point.PointLogRepository
import com.loopers.domain.point.PointRepository
import com.loopers.domain.user.User
import org.springframework.stereotype.Component

@Component
class PointPayProcessor(
    private val pointRepository: PointRepository,
    private val pointLogRepository: PointLogRepository,
) : PayProcessor {
    override val support: PayMethod = PayMethod.POINT

    override fun process(user: User, order: Order) {
        val point = pointRepository.getByUserId(user.id)

        point.use(order.totalAmount)

        pointLogRepository.save(PointLog.fromUse(user.id, point.id, order.totalAmount))
        pointRepository.save(point)
    }
}
