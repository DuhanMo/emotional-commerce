package com.loopers.domain.user

interface WalletRepository {
    fun findByUserId(userId: Long): Wallet?

    fun save(wallet: Wallet): Wallet
}
