package com.loopers.domain.support.cache

import com.fasterxml.jackson.core.type.TypeReference
import java.time.Duration

/**
 * 캐시 정책: TTL, null 캐시 여부/TTL, 버전 라벨 등
 */
data class CachePolicy(
    val ttl: Duration,
    val cacheNullAbsent: Boolean = true,
    val nullTtl: Duration = Duration.ofSeconds(30),
    val version: String? = null,
) {
    companion object {
        fun default() = CachePolicy(ttl = Duration.ofMinutes(5))
    }
}

/**
 * reified 헬퍼: 호출부에서 타입 안전한 TypeReference 생성
 */
inline fun <reified T> typeRef() = object : TypeReference<T>() {}
