package com.loopers.infrastructure.user

import com.loopers.domain.user.Wallet
import org.springframework.data.jpa.repository.JpaRepository

interface WalletJpaRepository : JpaRepository<Wallet, Long> {
    fun findByUserId(userId: Long): Wallet?
}
