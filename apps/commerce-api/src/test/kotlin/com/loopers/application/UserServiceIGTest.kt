package com.loopers.application

import com.loopers.application.user.UserService
import com.loopers.domain.user.User
import com.loopers.domain.user.UserCreateCommand
import com.loopers.domain.user.UserWriter
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.support.error.CoreException
import com.loopers.utils.DatabaseCleanUp
import com.ninjasquad.springmockk.SpykBean
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.mockk.verify
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest
class UserServiceIGTest @Autowired constructor(
    private val userService: UserService,
    private val userJpaRepository: UserJpaRepository,
    private val databaseCleanUp: DatabaseCleanUp,
    @SpykBean private val userWriter: UserWriter,
) : BehaviorSpec(
    {
        extensions(SpringExtension)

        afterTest {
            databaseCleanUp.truncateAllTables()
        }

        Given("이미 가입된 ID 가 없는 경우") {
            val command = createUserCreateCommand()

            When("회원 가입 하면") {
                userService.register(command)

                Then("User 저장이 수행된다") {
                    verify(exactly = 1) { userWriter.write(any()) }
                }
            }
        }

        Given("이미 가입된 ID 가 있는 경우") {
            val existUid = "test123"
            userJpaRepository.save(
                User(
                    uid = existUid,
                    email = "test@email.com",
                    birthDate = "2020-01-01",
                ),
            )
            val command = createUserCreateCommand(uid = existUid)

            When("회원 가입 하면") {
                Then("예외 발생한다") {
                    val exception = assertThrows<CoreException> {
                        userService.register(command)
                    }
                    exception.message shouldBe "이미 존재하는 ID 입니다"
                }
            }
        }
    },
)

private fun createUserCreateCommand(
    uid: String = "test123",
    email: String = "new@email.com",
    birthDate: String = "1999-12-25",
): UserCreateCommand = UserCreateCommand(
    uid = uid,
    email = email,
    birthDate = birthDate,
)
