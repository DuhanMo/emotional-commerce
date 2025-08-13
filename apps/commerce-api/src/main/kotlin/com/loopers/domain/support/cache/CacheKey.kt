package com.loopers.domain.support.cache

import java.time.Duration

/**
 * 캐시 키와 TTL(만료시간)을 함께 보유.
 * - namespace: 시스템/도메인 단위 네임스페이스
 * - key: 비즈니스 키(예: "product:123")
 * - ttl: 만료 시간 (null이면 무기한, 권장: 가능한 한 TTL 사용)
 * - TODO: jitterRatio: TTL에 랜덤 지터 비율(0.0~0.3 정도 권장) -> 동시 만료 완화
 */
data class CacheKey(
    val namespace: String,
    val key: String,
    val ttl: Duration = Duration.ofMinutes(10),
) {
    fun fullKey(): String = "$namespace::$key"
}
