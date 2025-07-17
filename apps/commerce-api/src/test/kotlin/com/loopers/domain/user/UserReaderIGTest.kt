package com.loopers.domain.user

import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.IntegrationTest
import com.loopers.utils.DatabaseCleanUp
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

@IntegrationTest
class UserReaderIGTest(
    private val userRepository: UserRepository,
    private val userJpaRepository: UserJpaRepository,
    private val databaseCleanUp: DatabaseCleanUp,
) : BehaviorSpec({
    afterEach {
        databaseCleanUp.truncateAllTables()
    }

    Given("ID가 일치하는 회원이 존재하는 경우") {
        val userReader = UserReader(userRepository)
        userJpaRepository.save(createUser(loginId = LoginId("abc123")))

        When("회원 정보를 조회하면") {
            val result = userReader.findByLoginId(LoginId("abc123"))

            Then("회원 정보를 반환한다") {
                result shouldNotBe null
                result!!.loginId shouldBe LoginId("abc123")
            }
        }
    }

    Given("ID가 일치하는 회원이 존재하지 않는 경우") {
        val userReader = UserReader(userRepository)
        userJpaRepository.save(createUser(loginId = LoginId("abc123")))

        When("회원 정보를 조회하면") {
            val result = userReader.findByLoginId(LoginId("xyz789"))

            Then("null을 반환한다") {
                result shouldBe null
            }
        }
    }
})
