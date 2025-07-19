package com.loopers.application

import com.loopers.application.user.UserService
import com.loopers.domain.user.Email
import com.loopers.domain.user.Gender
import com.loopers.domain.user.LoginId
import com.loopers.domain.user.UserReader
import com.loopers.domain.user.UserRegisterCommand
import com.loopers.domain.user.UserWriter
import com.loopers.domain.user.PointWriter
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.support.error.CoreException
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.IntegrationTest
import com.loopers.utils.DatabaseCleanUp
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.assertThrows

@IntegrationTest
class UserServiceIGTest(
    private val userWriter: UserWriter,
    private val userReader: UserReader,
    private val pointWriter: PointWriter,
    private val userJpaRepository: UserJpaRepository,
    private val databaseCleanUp: DatabaseCleanUp,
) : BehaviorSpec({
    afterEach {
        databaseCleanUp.truncateAllTables()
    }

    Given("이미 가입된 ID 가 없는 경우") {
        val userWriterSpy = spyk(userWriter)
        val pointWriterSpy = spyk(pointWriter)
        val userService = UserService(userWriterSpy, userReader, pointWriterSpy)
        val command = createUserCreateCommand()

        When("회원 가입 하면") {
            userService.register(command)

            Then("User 저장이 수행된다") {
                verify(exactly = 1) { userWriterSpy.write(any()) }
            }

            Then("Point 저장이 수행된다") {
                verify(exactly = 1) { pointWriterSpy.write(any()) }
            }
        }
    }

    Given("이미 가입된 ID 가 있는 경우") {
        val userService = UserService(userWriter, userReader, pointWriter)
        val existLoginId = LoginId("test123")
        userJpaRepository.save(createUser(loginId = existLoginId))
        val command = createUserCreateCommand(loginId = existLoginId)

        When("회원 가입 하면") {
            Then("예외 발생한다") {
                val exception = assertThrows<CoreException> {
                    userService.register(command)
                }
                exception.message shouldBe "이미 존재하는 ID 입니다"
            }
        }
    }
})

private fun createUserCreateCommand(
    loginId: LoginId = LoginId("test123"),
    email: Email = Email("test@test.com"),
    birthDate: String = "1999-12-25",
    gender: Gender = Gender.MALE,
): UserRegisterCommand = UserRegisterCommand(
    loginId = loginId,
    email = email,
    birthDate = birthDate,
    gender = gender,
)
