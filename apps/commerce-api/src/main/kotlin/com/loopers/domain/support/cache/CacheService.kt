package com.loopers.domain.support.cache

import java.time.Duration
import org.springframework.stereotype.Service

@Service
class CacheService(
    private val cacheRepository: CacheRepository,
) {
    /**
     * 캐시에서 조회하고 없으면 loader()로 로드 → 캐시에 저장 후 반환.
     *
     * @param cacheNullAbsent   true면 DB 에도 없을 때 '없음'을 짧은 TTL로 캐싱(캐시 관통 방지)
     * @param absentTtl         '없음' 상태 TTL (짧게 권장: 30~60초)
     */
    fun <T : Any> findOrLoad(
        key: CacheKey,
        clazz: Class<T>,
        cacheNullAbsent: Boolean = false,
        absentTtl: Duration = Duration.ofSeconds(30),
        loader: () -> T?,
    ): T? {
        cacheRepository.get(key, clazz)?.let { return it }

        val loaded = loader()

        if (loaded == null) {
            if (cacheNullAbsent) {
                // '없음'도 짧게 캐싱(관통 방지)
                cacheRepository.set(key.copy(ttl = absentTtl), NullMarker)
            }
            return null
        }

        cacheRepository.set(key, loaded)
        return loaded
    }

    /**
     * Write-aroundDB 갱신 후 관련 키 무효화
     */
    fun <R> writeAroundEvict(key: CacheKey, dbWrite: () -> R): R {
        val result = dbWrite()
        cacheRepository.evict(key)
        return result
    }

    /**
     * Write-through DB 갱신 후 캐시에 즉시 반영
     */
    fun <R : Any> writeThroughSet(key: CacheKey, dbWrite: () -> R, toCache: (R) -> Any = { it }): R {
        val result = dbWrite()
        cacheRepository.set(key, toCache(result))
        return result
    }

    fun evict(key: CacheKey): Boolean = cacheRepository.evict(key)
}
