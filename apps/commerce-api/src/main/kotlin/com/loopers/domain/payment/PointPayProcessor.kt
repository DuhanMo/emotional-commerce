package com.loopers.domain.payment

import com.loopers.domain.order.Order
import com.loopers.domain.order.PayMethod
import com.loopers.domain.point.PointRepository
import com.loopers.domain.user.User
import org.springframework.stereotype.Component

@Component
class PointPayProcessor(
    private val pointRepository: PointRepository,
) : PayProcessor {
    override val support: PayMethod = PayMethod.POINT

    override fun process(user: User, order: Order) {
        val point = pointRepository.getByUserId(user.id)

        point.use(order.totalAmount)

        pointRepository.save(point)
    }
}
