package com.loopers.application

import com.loopers.application.user.WalletService
import com.loopers.domain.user.ChargePointCommand
import com.loopers.domain.user.LoginId
import com.loopers.domain.user.Wallet
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.infrastructure.user.WalletJpaRepository
import com.loopers.support.error.CoreException
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.IntegrationTest
import com.loopers.utils.DatabaseCleanUp
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.data.repository.findByIdOrNull

@IntegrationTest
class WalletServiceIGTest(
    private val walletService: WalletService,
    private val databaseCleanUp: DatabaseCleanUp,
    private val userJpaRepository: UserJpaRepository,
    private val walletJpaRepository: WalletJpaRepository,
) : BehaviorSpec({
    afterEach {
        databaseCleanUp.truncateAllTables()
    }

    Given("로그인 ID가 존재하지 않는 경우") {
        val user = userJpaRepository.save(createUser(loginId = LoginId("abc123")))
        walletJpaRepository.save(Wallet(user))

        When("포인트를 충전하면") {
            Then("예외 발생한다") {
                shouldThrow<CoreException> {
                    walletService.charge(ChargePointCommand(LoginId("xyz789"), 100))
                }
            }
        }
    }

    Given("로그인 ID가 존재하는 경우") {
        val user = userJpaRepository.save(createUser(loginId = LoginId("abc123")))
        val wallet = walletJpaRepository.save(Wallet(user))

        When("포인트를 충전하면") {
            walletService.charge(ChargePointCommand(LoginId("abc123"), 100))

            Then("포인트가 증가한다") {
                val foundWallet = walletJpaRepository.findByIdOrNull(wallet.id)!!
                foundWallet.point shouldBe 100
            }
        }
    }
})








