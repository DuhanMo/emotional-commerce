package com.loopers.domain.user

@JvmInline
value class LoginId(val value: String) {
    init {
        require(ID_PATTERN.matches(value)) { "ID는 영문과 숫자를 모두 포함한 10자 이하여야 합니다." }
    }

    companion object {
        private val ID_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{1,10}$".toRegex()
    }
}
