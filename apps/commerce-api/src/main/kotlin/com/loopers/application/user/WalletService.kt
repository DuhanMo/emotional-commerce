package com.loopers.application.user

import com.loopers.domain.user.ChargePointCommand
import com.loopers.domain.user.LoginId
import com.loopers.domain.user.WalletReader
import com.loopers.domain.user.WalletWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WalletService(
    private val walletReader: WalletReader,
    private val walletWriter: WalletWriter,
) {
    @Transactional
    fun charge(command: ChargePointCommand): WalletOutput {
        val wallet = walletReader.getByLoginId(command.loginId)

        wallet.charge(command.point)

        return WalletOutput.from(walletWriter.write(wallet))
    }

    fun find(loginId: LoginId): WalletOutput = WalletOutput.from(walletReader.getByLoginId(loginId))
}
