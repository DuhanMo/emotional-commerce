package com.loopers.domain.support.cache

import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod

object CacheKeyUtil {
    fun build(
        function: KFunction<*>,
        vararg params: Any?,
    ): String {
        val className = function.javaMethod?.declaringClass?.simpleName ?: "UnknownClass"
        val methodName = function.name
        return buildString {
            append(className)
            append(".")
            append(methodName)
            params.forEach {
                append("|")
                append(it ?: "null")
            }
        }
    }
}
