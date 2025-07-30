package com.loopers.application.product

import com.loopers.domain.user.LoginId

data class LikeProductInput(
    val productId: Long,
    val loginId: LoginId,
)
