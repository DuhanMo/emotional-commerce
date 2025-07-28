package com.loopers.domain.point

import com.loopers.domain.user.LoginId
import com.loopers.infrastructure.point.PointJpaRepository
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class PointReaderIGTest(
    private val pointReader: PointReader,
    private val userJapRepository: UserJpaRepository,
    private val pointJpaRepository: PointJpaRepository,
) : IntegrationSpec({
    Given("해당 ID 의 회원이 존재하는 경우") {
        val user = userJapRepository.save(createUser(LoginId("abc123")))
        pointJpaRepository.save(Point(user.id, 1000))

        When("포인트를 조회하면") {
            val result = pointReader.findByLoginId(user.loginId)

            Then("포인트를 반환한다") {
                result shouldNotBe null
                result?.amount shouldBe 1000
            }
        }
    }

    Given("해당 ID 의 회원이 존재하지 않는 경우") {
        val user = userJapRepository.save(createUser(LoginId("abc123")))
        pointJpaRepository.save(Point(user.id, 1000))

        When("포인트를 조회하면") {
            val result = pointReader.findByLoginId(LoginId("xyz789"))

            Then("null 을 반환한다") {
                result shouldBe null
            }
        }
    }
})
