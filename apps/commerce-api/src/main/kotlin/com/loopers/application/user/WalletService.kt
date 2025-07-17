package com.loopers.application.user

import com.loopers.domain.user.ChargePointCommand
import com.loopers.domain.user.UserReader
import com.loopers.domain.user.WalletReader
import com.loopers.domain.user.WalletWriter
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WalletService(
    private val userReader: UserReader,
    private val walletReader: WalletReader,
    private val walletWriter: WalletWriter,
) {
    @Transactional
    fun charge(command: ChargePointCommand) {
        val user = userReader.getLoginId(command.loginId)
        val wallet = walletReader.getByUserId(user.id)

        wallet.charge(command.point)

        walletWriter.write(wallet)
    }
}
