package com.commerce.duhan.application.user

import com.commerce.duhan.domain.user.Gender
import com.commerce.duhan.domain.user.LoginId
import com.commerce.duhan.domain.user.UserRepository
import com.commerce.duhan.domain.fixtures.TEST_BIRTH_DATE
import com.commerce.duhan.domain.fixtures.TEST_EMAIL
import com.commerce.duhan.domain.fixtures.TEST_LOGIN_ID
import com.commerce.duhan.domain.fixtures.createUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserWriterTest : DescribeSpec({
    val userRepository = mockk<UserRepository>()
    val underTest = UserCreator(userRepository)

    describe("유저 생성") {
        context("이미 존재하는 로그인 아이디인 경우") {
            it("생성에 실패한다") {
                // given
                val existLoginId = LoginId("exist123")
                every { userRepository.existByLoginId(existLoginId) } returns true

                // when & then
                shouldThrow<IllegalArgumentException> {
                    underTest.create(
                        CreateUserCommand(
                            loginId = existLoginId,
                            email = TEST_EMAIL,
                            birthDate = TEST_BIRTH_DATE,
                            gender = Gender.MALE,
                        ),
                    )
                }
            }
        }

        context("존재하지 않는 로그인 아이디인 경우") {
            it("생성에 성공한다") {
                // given
                every { userRepository.existByLoginId(TEST_LOGIN_ID) } returns false
                every { userRepository.save(any()) } returns createUser()

                // when
                underTest.create(
                    CreateUserCommand(
                        loginId = TEST_LOGIN_ID,
                        email = TEST_EMAIL,
                        birthDate = TEST_BIRTH_DATE,
                        gender = Gender.MALE,
                    ),
                )

                // then
                verify(exactly = 1) { userRepository.save(any()) }
            }
        }
    }
    afterTest {
        clearAllMocks()
    }
})
