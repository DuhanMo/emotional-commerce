package com.loopers.domain.user

import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.support.error.CoreException
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.IntegrationSpec
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class UserQueryServiceIGTest(
    private val userRepository: UserRepository,
    private val userJpaRepository: UserJpaRepository,
) : IntegrationSpec({
    Given("ID가 일치하는 회원이 존재하는 경우") {
        val userQueryService = UserQueryService(userRepository)
        userJpaRepository.save(createUser(loginId = LoginId("abc123")))

        When("회원 정보를 조회하면") {
            val result = userQueryService.getByLoginId(LoginId("abc123"))

            Then("회원 정보를 반환한다") {
                result shouldNotBe null
                result.loginId shouldBe LoginId("abc123")
            }
        }
    }

    Given("ID가 일치하는 회원이 존재하지 않는 경우") {
        val userQueryService = UserQueryService(userRepository)
        userJpaRepository.save(createUser(loginId = LoginId("abc123")))

        When("회원 정보를 조회하면") {
            Then("예외발생한다") {
                shouldThrow<CoreException> {
                    userQueryService.getByLoginId(LoginId("xyz789"))
                }
            }
        }
    }
})
