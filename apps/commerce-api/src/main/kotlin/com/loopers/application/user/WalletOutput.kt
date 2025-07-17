package com.loopers.application.user

import com.loopers.domain.user.Wallet

data class WalletOutput(
    val point: Int,
) {
    companion object {
        fun from(wallet: Wallet): WalletOutput = WalletOutput(wallet.point)
    }
}
