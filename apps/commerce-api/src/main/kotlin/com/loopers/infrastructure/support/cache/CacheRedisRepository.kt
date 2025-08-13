package com.loopers.infrastructure.support.cache

import com.fasterxml.jackson.databind.ObjectMapper
import com.loopers.config.redis.RedisConfig
import com.loopers.domain.support.cache.CacheKey
import com.loopers.domain.support.cache.CacheRepository
import com.loopers.domain.support.cache.NullMarker
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class CacheRedisRepository(
    private val objectMapper: ObjectMapper,
    // 읽기: replica preferred (default)
    private val redis: RedisTemplate<String, String>,
    // 쓰기/삭제: master 강제
    @Qualifier(RedisConfig.REDIS_TEMPLATE_MASTER)
    private val redisMaster: RedisTemplate<String, String>,
) : CacheRepository {
    override fun <T : Any> get(key: CacheKey, clazz: Class<T>): T? {
        val raw = redis.opsForValue().get(key.fullKey()) ?: return null
        if (raw == NULL_MARKER_JSON) return null
        return runCatching {
            objectMapper.readValue(raw, clazz)
        }.getOrNull()
    }

    override fun <T : Any> set(key: CacheKey, value: T) {
        val json = if (value === NullMarker) {
            NULL_MARKER_JSON
        } else {
            objectMapper.writeValueAsString(value)
        }
        redisMaster.opsForValue().set(key.fullKey(), json, key.ttl)
    }

    override fun evict(key: CacheKey): Boolean =
        redisMaster.delete(key.fullKey())

    companion object {
        private const val NULL_MARKER_JSON = """{"__null__":true}"""
    }
}
