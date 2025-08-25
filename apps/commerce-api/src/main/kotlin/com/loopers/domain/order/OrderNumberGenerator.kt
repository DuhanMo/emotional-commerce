package com.loopers.domain.order

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

object OrderNumberGenerator {
    private val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    private val seoulZone = ZoneId.of("Asia/Seoul")

    fun generate(): String {
        val timestamp = LocalDateTime.now(seoulZone).format(formatter)
        val randomPart = UUID.randomUUID().toString().substring(0, 6)
        return "$timestamp$randomPart"
    }
}
