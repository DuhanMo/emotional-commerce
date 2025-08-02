package com.loopers.application.product

import com.loopers.domain.user.LoginId

data class UnlikeProductInput(
    val productId: Long,
    val loginId: LoginId,
)
