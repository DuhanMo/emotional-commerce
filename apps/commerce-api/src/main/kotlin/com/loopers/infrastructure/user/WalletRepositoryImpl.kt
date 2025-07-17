package com.loopers.infrastructure.user

import com.loopers.domain.user.Wallet
import com.loopers.domain.user.WalletRepository
import org.springframework.stereotype.Repository

@Repository
class WalletRepositoryImpl(
    private val walletJpaRepository: WalletJpaRepository,
) : WalletRepository {
    override fun findByUserId(userId: Long): Wallet? = walletJpaRepository.findByUserId(userId)

    override fun save(wallet: Wallet): Wallet = walletJpaRepository.save(wallet)
}
