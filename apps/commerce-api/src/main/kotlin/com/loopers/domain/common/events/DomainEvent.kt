package com.loopers.domain.common.events

import java.time.LocalDateTime
import java.util.UUID

interface DomainEvent {
    val eventId: String
    val occurredAt: LocalDateTime
    val eventType: String
}

abstract class BaseDomainEvent : DomainEvent {
    override val eventId: String = UUID.randomUUID().toString()
    override val occurredAt: LocalDateTime = LocalDateTime.now()
}
