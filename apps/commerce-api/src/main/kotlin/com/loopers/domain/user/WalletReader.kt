package com.loopers.domain.user

import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Component

@Component
class WalletReader(
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository,
) {
    fun getByLoginId(loginId: LoginId): Wallet = findByLoginId(loginId)
        ?: throw CoreException(ErrorType.NOT_FOUND, "존재하지 않는 지갑입니다")

    fun findByLoginId(loginId: LoginId): Wallet? =
        userRepository.findByLoginId(loginId)?.let { user ->
            walletRepository.findByUserId(user.id)
        }
}
