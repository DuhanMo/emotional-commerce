package com.loopers.domain.user

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class WalletWriter(
    private val walletRepository: WalletRepository,
) {
    @Transactional
    fun write(wallet: Wallet): Wallet = walletRepository.save(wallet)
}
