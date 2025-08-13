package com.loopers.domain.support.cache

interface CacheRepository {
    fun <T : Any> get(key: CacheKey, clazz: Class<T>): T?
    fun <T : Any> set(key: CacheKey, value: T)
    fun evict(key: CacheKey): Boolean
}
