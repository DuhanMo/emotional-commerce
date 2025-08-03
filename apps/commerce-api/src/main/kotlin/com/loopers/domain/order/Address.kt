package com.loopers.domain.order

import jakarta.persistence.Embeddable

@Embeddable
data class Address(
    val street: String,
    val city: String,
    val zipCode: String,
    val detailAddress: String? = null,
) {
    init {
        require(street.isNotBlank()) { "도로명은 필수입니다." }
        require(city.isNotBlank()) { "도시명은 필수입니다." }
        require(zipCode.isNotBlank()) { "우편번호는 필수입니다." }
        require(zipCode.matches(Regex("\\d{5}"))) { "우편번호는 5자리 숫자여야 합니다." }
    }
}
