package com.commerce.duhan.integration.user

import com.commerce.duhan.core.application.user.UserFinder
import com.commerce.duhan.core.domain.user.BirthDate
import com.commerce.duhan.core.domain.user.Email
import com.commerce.duhan.core.domain.user.Gender
import com.commerce.duhan.core.domain.user.LoginId
import com.commerce.duhan.core.domain.user.User
import com.commerce.duhan.db.user.UserJpaRepository
import com.commerce.duhan.support.IntegrationSpec
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

class UserFinderIGTest(
    private val underTest: UserFinder,
    private val userJpaRepository: UserJpaRepository,
) : IntegrationSpec({
    describe("유저 조회") {
        context("유저가 존재하는 경우") {
            it("유저를 반환한다") {
                // given
                val user = userJpaRepository.save(
                    User(
                        loginId = LoginId("test123"),
                        email = Email("test@test.com"),
                        birthDate = BirthDate("2020-01-01"),
                        gender = Gender.MALE,
                    ),
                )

                // when
                val result = underTest.getById(user.id)

                // then
                result.id shouldBe user.id
                result.loginId shouldBe LoginId("test123")
                result.email shouldBe Email("test@test.com")
                result.birthDate shouldBe BirthDate("2020-01-01")
                result.gender shouldBe Gender.MALE
            }
        }

        context("유저가 존재하지 않는 경우") {
            it("예외가 발생한다") {
                // when & then
                shouldThrow<NoSuchElementException> {
                    underTest.getById(2L)
                }
            }
        }
    }
})
