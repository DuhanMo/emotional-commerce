package com.commerce.duhan.application.user

import com.commerce.duhan.application.point.PointCreator
import com.commerce.duhan.application.point.PointFinder
import com.commerce.duhan.domain.fixtures.TEST_BIRTH_DATE
import com.commerce.duhan.domain.fixtures.TEST_EMAIL
import com.commerce.duhan.domain.fixtures.TEST_LOGIN_ID
import com.commerce.duhan.domain.fixtures.createUser
import com.commerce.duhan.domain.user.Gender
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.assertThrows

class UserApplicationServiceTest : DescribeSpec({
    val userCreator = mockk<UserCreator>()
    val pointWriter = mockk<PointCreator>()
    val userFinder = mockk<UserFinder>()
    val pointFinder = mockk<PointFinder>()
    val underTest = UserApplicationService(userCreator, pointWriter, userFinder, pointFinder)

    describe("유저 생성") {
        context("유저 생성에 실패하는 경우") {
            it("포인트 생성이 호출되지 않는다") {
                // given
                every { userCreator.create(any()) } throws IllegalArgumentException()

                // when
                assertThrows<IllegalArgumentException> {
                    underTest.create(
                        CreateUserCommand(
                            loginId = TEST_LOGIN_ID,
                            email = TEST_EMAIL,
                            birthDate = TEST_BIRTH_DATE,
                            gender = Gender.MALE,
                        ),
                    )
                }
                // then
                verify(exactly = 0) { pointWriter.create(any()) }
            }
        }

        context("유저 생성에 성공하는 경우") {
            it("포인트 생성이 호출된다") {
                // given
                every { userCreator.create(any()) } returns createUser()
                every { pointWriter.create(any()) } just runs

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
                verify(exactly = 1) { pointWriter.create(any()) }
            }
        }
    }

    afterTest {
        clearAllMocks()
    }
})
