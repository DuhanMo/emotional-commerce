package com.loopers.interfaces.api.wallet

import com.loopers.application.user.WalletOutput

data class PointChargeResponse(
    val point: Int,
) {
    companion object {
        fun from(output: WalletOutput): PointChargeResponse = PointChargeResponse(output.point)
    }
}
