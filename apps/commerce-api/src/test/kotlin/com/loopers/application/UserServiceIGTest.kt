package com.loopers.application

import com.loopers.application.user.UserService
import com.loopers.domain.user.Gender
import com.loopers.domain.user.User
import com.loopers.domain.user.UserId
import com.loopers.domain.user.UserReader
import com.loopers.domain.user.UserRegisterCommand
import com.loopers.domain.user.UserWriter
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.support.error.CoreException
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
    private val userJpaRepository: UserJpaRepository,
    private val databaseCleanUp: DatabaseCleanUp,
) : BehaviorSpec({
    afterTest {
        databaseCleanUp.truncateAllTables()
    }

    Given("이미 가입된 ID 가 없는 경우") {
        val userWriterSpy = spyk(userWriter)
        val userService = UserService(userWriterSpy, userReader)
        val command = createUserCreateCommand()

        When("회원 가입 하면") {
            userService.register(command)

            Then("User 저장이 수행된다") {
                verify(exactly = 1) { userWriterSpy.write(any()) }
            }
        }
    }

    Given("이미 가입된 ID 가 있는 경우") {
        val userService = UserService(userWriter, userReader)
        val existUserId = UserId("test123")
        userJpaRepository.save(
            User(
                userId = existUserId,
                email = "test@test.com",
                birthDate = "2020-01-01",
                gender = Gender.MALE,
            ),
        )
        val command = createUserCreateCommand(userId = existUserId)

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
    userId: UserId = UserId("test123"),
    email: String = "new@email.com",
    birthDate: String = "1999-12-25",
    gender: Gender = Gender.MALE,
): UserRegisterCommand = UserRegisterCommand(
    userId = userId,
    email = email,
    birthDate = birthDate,
    gender = gender,
)
