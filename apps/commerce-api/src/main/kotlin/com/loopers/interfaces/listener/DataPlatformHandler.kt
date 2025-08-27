package com.loopers.interfaces.listener

import com.loopers.domain.common.events.OrderCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class DataPlatformHandler {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    fun handle(event: OrderCreatedEvent) {
        try {
            // TODO: 데이터 플랫폼에 전송
        } catch (e: Exception) {
            logger.error("데이터 플랫폼 전송 실패.(eventType: ${event.eventType})", e)
            // TODO: 알림 또는
            //      1. Retry, Recover 추가
            //      2. FailedEvent 데이터 베이스 적재 후 추후 재시도
            //      3. DLQ 발행
        }
    }
}
