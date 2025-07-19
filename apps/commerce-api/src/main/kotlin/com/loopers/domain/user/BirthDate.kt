package com.loopers.domain.user

import java.time.LocalDate

@JvmInline
value class BirthDate(val value: String) {
    init {
        runCatching {
            BIRTH_DATE_PATTERN.matches(value)
            LocalDate.parse(value)
        }.getOrElse {
            throw IllegalArgumentException("생년월일은 yyyy-MM-dd 형식이어야 합니다.")
        }
    }

    companion object {
        private val BIRTH_DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$".toRegex()
    }
}
