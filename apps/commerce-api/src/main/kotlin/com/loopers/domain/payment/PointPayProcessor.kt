package com.loopers.domain.payment

import com.loopers.domain.point.PointHistory
import com.loopers.domain.point.PointHistoryRepository
import com.loopers.domain.point.PointRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class PointPayProcessor(
    private val pointRepository: PointRepository,
    private val pointHistoryRepository: PointHistoryRepository,
) : PayProcessor {
    override fun support() = PaymentMethod.POINT

    @Transactional
    override fun process(command: RequestPaymentCommand): Transaction {
        val point = pointRepository.getByUserIdWithLock(command.userId)
        point.use(command.amount)

        pointRepository.save(point)
        pointHistoryRepository.save(PointHistory.fromUse(command.userId, point.id, command.amount))
        return Transaction(
            transactionKey = "point-transaction-order-${command.orderNumber}",
            status = TransactionStatus.SUCCESS,
            reason = "포인트 결제",
        )
    }
}
