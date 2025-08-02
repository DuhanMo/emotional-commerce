package com.loopers.domain.order

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll

class AddressTest : StringSpec({

    "도로명이 공백인 경우, 주소 객체 생성에 실패한다" {
        listOf("", " ", "  ").forAll { invalidStreet ->
            shouldThrow<IllegalArgumentException> {
                Address(
                    street = invalidStreet,
                    city = "서울시",
                    zipCode = "12345",
                    detailAddress = "상세주소",
                )
            }
        }
    }

    "도시명이 공백인 경우, 주소 객체 생성에 실패한다" {
        listOf("", " ", "  ").forAll { invalidCity ->
            shouldThrow<IllegalArgumentException> {
                Address(
                    street = "강남대로",
                    city = invalidCity,
                    zipCode = "12345",
                    detailAddress = "상세주소",
                )
            }
        }
    }

    "우편번호가 공백인 경우, 주소 객체 생성에 실패한다" {
        listOf("", " ", "  ").forAll { invalidZipCode ->
            shouldThrow<IllegalArgumentException> {
                Address(
                    street = "강남대로",
                    city = "서울시",
                    zipCode = invalidZipCode,
                    detailAddress = "상세주소",
                )
            }
        }
    }

    "우편번호가 5자리 숫자가 아닌 경우, 주소 객체 생성에 실패한다" {
        listOf(
            "1234",
            "123456",
            "abcde",
            "1234a",
            "12-345",
        ).forAll { invalidZipCode ->
            shouldThrow<IllegalArgumentException> {
                Address(
                    street = "강남대로",
                    city = "서울시",
                    zipCode = invalidZipCode,
                    detailAddress = "상세주소",
                )
            }
        }
    }

    "우편번호가 5자리 숫자인 경우, 주소 객체 생성에 성공한다" {
        listOf("12345", "00000", "99999").forAll { validZipCode ->
            shouldNotThrow<IllegalArgumentException> {
                Address(
                    street = "강남대로",
                    city = "서울시",
                    zipCode = validZipCode,
                    detailAddress = "상세주소",
                )
            }
        }
    }

    "상세주소가 null인 경우에도 주소 객체 생성에 성공한다" {
        shouldNotThrow<IllegalArgumentException> {
            Address(
                street = "강남대로",
                city = "서울시",
                zipCode = "12345",
                detailAddress = null,
            )
        }
    }

    "모든 필수 필드가 올바른 경우, 주소 객체 생성에 성공한다" {
        shouldNotThrow<IllegalArgumentException> {
            Address(
                street = "강남대로 123",
                city = "서울시 강남구",
                zipCode = "06234",
                detailAddress = "삼성동 롯데월드타워 10층",
            )
        }
    }
})
