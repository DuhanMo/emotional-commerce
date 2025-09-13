package com.commerce.duhan.core.domain.user

import java.time.LocalDate

@JvmInline
value class LoginId(val value: String) {
    init {
        require(ID_PATTERN.matches(value)) { "ID는 영문과 숫자를 모두 포함한 10자 이하여야 합니다." }
    }

    companion object {
        private val ID_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{1,10}$".toRegex()
    }
}

@JvmInline
value class Email(val value: String) {
    init {
        require(EMAIL_PATTERN.matches(value)) { "이메일이 형식에 맞지 않습니다." }
    }

    companion object {
        private val EMAIL_PATTERN = "^[^@]+@[^@]+\\.[^@]+$".toRegex()
    }
}

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
