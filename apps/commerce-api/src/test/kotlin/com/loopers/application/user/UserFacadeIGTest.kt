package com.loopers.application.user

import com.loopers.domain.user.BirthDate
import com.loopers.domain.user.Email
import com.loopers.domain.user.Gender
import com.loopers.domain.user.LoginId
import com.loopers.domain.user.UserQueryService
import com.loopers.domain.user.UserService
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.support.error.CoreException
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.shouldBe
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.assertThrows

class UserFacadeIGTest(
    private val userService: UserService,
    private val userQueryService: UserQueryService,
    private val userJpaRepository: UserJpaRepository,
) : IntegrationSpec({
    Given("이미 가입된 ID 가 없는 경우") {
        val userWriterSpy = spyk(userService)
        val userFacade = UserFacade(userWriterSpy, userQueryService)
        val command = createUserCreateInput()

        When("회원 가입 하면") {
            userFacade.register(command)

            Then("유저 저장이 수행된다") {
                verify(exactly = 1) { userWriterSpy.createUser(any()) }
            }
        }
    }

    Given("이미 가입된 ID 가 있는 경우") {
        val userFacade = UserFacade(userService, userQueryService)
        val existLoginId = LoginId("test123")
        userJpaRepository.save(createUser(loginId = existLoginId))
        val command = createUserCreateInput(loginId = existLoginId)

        When("회원 가입 하면") {
            Then("예외 발생한다") {
                val exception = assertThrows<CoreException> {
                    userFacade.register(command)
                }
                exception.message shouldBe "이미 존재하는 ID 입니다"
            }
        }
    }
})

private fun createUserCreateInput(
    loginId: LoginId = LoginId("test123"),
    email: Email = Email("test@test.com"),
    birthDate: BirthDate = BirthDate("1999-12-25"),
    gender: Gender = Gender.MALE,
): UserRegisterInput = UserRegisterInput(
    loginId = loginId,
    email = email,
    birthDate = birthDate,
    gender = gender,
)
