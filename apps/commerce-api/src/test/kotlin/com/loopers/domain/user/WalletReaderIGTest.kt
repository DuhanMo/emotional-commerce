package com.loopers.domain.user

import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.infrastructure.user.WalletJpaRepository
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.IntegrationTest
import com.loopers.utils.DatabaseCleanUp
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

@IntegrationTest
class WalletReaderIGTest(
    private val walletReader: WalletReader,
    private val userJapRepository: UserJpaRepository,
    private val walletJpaRepository: WalletJpaRepository,
    private val databaseCleanUp: DatabaseCleanUp,
) : BehaviorSpec({
    afterEach {
        databaseCleanUp.truncateAllTables()
    }

    Given("해당 ID 의 회원이 존재하는 경우") {
        val user = userJapRepository.save(createUser(LoginId("abc123")))
        walletJpaRepository.save(Wallet(user, 1000))

        When("지갑을 조회하면") {
            val result = walletReader.findByLoginId(user.loginId)

            Then("지갑을 반환한다") {
                result shouldNotBe null
                result?.point shouldBe 1000
            }
        }
    }

    Given("해당 ID 의 회원이 존재하지 않는 경우") {
        val user = userJapRepository.save(createUser(LoginId("abc123")))
        walletJpaRepository.save(Wallet(user, 1000))

        When("지갑을 조회하면") {
            val result = walletReader.findByLoginId(LoginId("xyz789"))

            Then("null 을 반환한다") {
                result shouldBe null
            }
        }
    }
})
