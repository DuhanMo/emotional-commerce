package com.loopers.application.payment

import com.loopers.domain.payment.Payment
import com.loopers.domain.payment.PaymentMethod
import com.loopers.domain.payment.PaymentRepository
import com.loopers.domain.point.PointHistory
import com.loopers.domain.point.PointHistoryRepository
import com.loopers.domain.point.PointRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PointPaymentProcessor(
    private val pointRepository: PointRepository,
    private val pointHistoryRepository: PointHistoryRepository,
    private val paymentRepository: PaymentRepository,
) : PaymentProcessor {
    override fun support(): PaymentMethod = PaymentMethod.POINT

    @Transactional
    override fun process(command: RequestPaymentCommand) {
        val point = pointRepository.getByUserIdWithLock(command.userId)

        point.use(command.amount)
        val pointHistory = PointHistory.fromUse(point.userId, point.id, point.amount)

        val payment = Payment.requestByPoint(
            userId = command.userId,
            orderId = command.orderId,
            idempotencyKey = command.idempotencyKey,
            method = command.paymentMethod,
            amount = command.amount,
        )

        pointHistoryRepository.save(pointHistory)
        pointRepository.save(point)
        paymentRepository.save(payment)
    }
}
