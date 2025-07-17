package com.loopers.domain.user

import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Component

@Component
class WalletReader(
    private val walletRepository: WalletRepository,
) {
    fun getByUserId(userId: Long): Wallet = walletRepository.findByUserId(userId)
        ?: throw CoreException(ErrorType.NOT_FOUND, "존재하지 않는 지갑입니다")
}
