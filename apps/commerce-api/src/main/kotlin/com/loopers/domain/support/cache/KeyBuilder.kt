package com.loopers.domain.support.cache

/**
 * 키 생성 유틸.
 * - namespace : 논리 그룹(필수)
 * - version   : 스키마/직렬화 버전(선택). 변경 시 대량 evict 없이 키 교체 가능
 * - parts     : 식별자/조건(brandId, sort, page 등)
 */
object KeyBuilder {
    fun build(namespace: String, vararg parts: Any?, version: String? = null): String =
        buildString {
            append(namespace)
            if (!version.isNullOrBlank()) {
                append(":v=").append(version)
            }
            parts.forEach { p ->
                append(':').append(normalize(p))
            }
        }

    private fun normalize(part: Any?): String = when (part) {
        null -> "null"
        is String -> part.trim().ifEmpty { "empty" }
        else -> part.toString()
    }
}
