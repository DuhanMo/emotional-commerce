package com.loopers.domain.user

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.format.DateTimeParseException

@Table(name = "users")
@Entity
class User(
    val uid: String,
    val email: String,
    val birthDate: String,
    @Enumerated(EnumType.STRING)
    val gender: Gender,
) : BaseEntity() {
    init {
        validate()
    }

    private fun validate() {
        require(ID_PATTERN.matches(uid)) { "ID는 영문과 숫자를 모두 포함한 10자 이하여야 합니다." }
        require(EMAIL_PATTERN.matches(email)) { "이메일이 형식에 맞지 않습니다." }
        try {
            BIRTH_DATE_PATTERN.matches(birthDate)
            LocalDate.parse(birthDate)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("생년월일은 yyyy-MM-dd 형식이어야 합니다.")
        }
    }

    companion object {
        private val ID_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]{1,10}$".toRegex()
        private val EMAIL_PATTERN = "^[^@]+@[^@]+\\.[^@]+$".toRegex()
        private val BIRTH_DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$".toRegex()
    }
}
