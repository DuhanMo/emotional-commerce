package com.loopers.domain.user

@JvmInline
value class Email(val value: String) {
    init {
        require(EMAIL_PATTERN.matches(value)) { "이메일이 형식에 맞지 않습니다." }
    }

    companion object {
        private val EMAIL_PATTERN = "^[^@]+@[^@]+\\.[^@]+$".toRegex()
    }
}
