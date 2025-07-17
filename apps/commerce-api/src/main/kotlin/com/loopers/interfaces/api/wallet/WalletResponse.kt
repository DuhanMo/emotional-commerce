package com.loopers.interfaces.api.wallet

import com.loopers.application.user.WalletOutput

data class WalletResponse(
    val point: Int,
) {
    companion object {
        fun from(output: WalletOutput): WalletResponse = WalletResponse(output.point)
    }
}
