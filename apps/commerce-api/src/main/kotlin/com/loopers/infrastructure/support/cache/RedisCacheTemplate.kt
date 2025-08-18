// module: infrastructure (예: com.loopers.infrastructure.support.cache)
// file: RedisCacheTemplate.kt
package com.loopers.infrastructure.support.cache

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.loopers.config.redis.RedisConfig
import com.loopers.domain.support.cache.CachePolicy
import com.loopers.domain.support.cache.CacheTemplate
import java.time.Duration
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisCacheTemplate(
    private val objectMapper: ObjectMapper,
    private val redis: RedisTemplate<String, String>,
    @param:Qualifier(RedisConfig.REDIS_TEMPLATE_MASTER)
    private val redisMaster: RedisTemplate<String, String>,
) : CacheTemplate {
    override fun <T : Any> get(key: String, type: TypeReference<T>): T? {
        val raw = redis.opsForValue().get(key) ?: return null
        if (raw == NULL_MARKER_JSON) return null
        return objectMapper.readValue(raw, type)
    }

    override fun set(key: String, value: Any, ttl: Duration) {
        val json = objectMapper.writeValueAsString(value)
        redisMaster.opsForValue().set(key, json, ttl)
    }

    override fun evict(key: String): Boolean =
        redisMaster.delete(key)

    override fun <T : Any> findOrLoad(
        key: String,
        type: TypeReference<T>,
        policy: CachePolicy,
        loader: () -> T?,
    ): T? {
        get(key, type)?.let { return it }

        val loaded = loader()

        if (loaded == null) {
            if (policy.cacheNullAbsent) {
                redisMaster.opsForValue().set(key, NULL_MARKER_JSON, policy.nullTtl)
            }
            return null
        }

        set(key, loaded, policy.ttl)
        return loaded
    }

    companion object {
        private const val NULL_MARKER_JSON = """{"__null__":true}"""
    }
}
