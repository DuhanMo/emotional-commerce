package com.loopers.infrastructure.payment

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class PgApiResponse<T>(
    val meta: Meta,
    val data: T?,
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Meta(
        val result: Result,
        val errorCode: String?,
        val message: String?,
    ) {
        enum class Result { SUCCESS, FAIL }
    }

    val isSuccess: Boolean
        get() = meta.result == Meta.Result.SUCCESS
}
