package com.loopers.domain.support.cache

import com.fasterxml.jackson.core.type.TypeReference
import java.time.Duration

/**
 * 애플리케이션 레이어에서 사용하는 캐시 추상화.
 * - get/set/evict 기본 연산
 * - Cache-Aside 패턴(findOrLoad)
 */
interface CacheTemplate {
    fun <T : Any> get(key: String, type: TypeReference<T>): T?
    fun set(key: String, value: Any, ttl: Duration)
    fun evict(key: String): Boolean

    /**
     * Cache-Aside:
     * 1) 캐시 조회
     * 2) miss면 loader() 수행
     * 3) 결과를 캐시에 저장 후 반환
     * 4) loader 결과가 null이면 (원하면) null 마커를 짧게 캐싱하여 관통 방지
     */
    fun <T : Any> findOrLoad(
        key: String,
        type: TypeReference<T>,
        policy: CachePolicy = CachePolicy.default(),
        loader: () -> T?,
    ): T?
}
