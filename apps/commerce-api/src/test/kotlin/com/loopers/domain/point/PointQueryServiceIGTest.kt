 package com.loopers.domain.point

 import com.loopers.domain.support.Money
 import com.loopers.domain.user.LoginId
 import com.loopers.infrastructure.point.PointJpaRepository
 import com.loopers.infrastructure.user.UserJpaRepository
 import com.loopers.support.error.CoreException
 import com.loopers.support.fixture.createUser
 import com.loopers.support.tests.IntegrationSpec
 import io.kotest.assertions.throwables.shouldThrow
 import io.kotest.matchers.shouldBe
 import io.kotest.matchers.shouldNotBe

 class PointQueryServiceIGTest(
    private val pointQueryService: PointQueryService,
    private val userJapRepository: UserJpaRepository,
    private val pointJpaRepository: PointJpaRepository,
 ) : IntegrationSpec({
    Given("해당 ID 의 회원이 존재하는 경우") {
        val user = userJapRepository.save(createUser(LoginId("abc123")))
        pointJpaRepository.save(Point(user.id, Money(1_000)))

        When("포인트를 조회하면") {
            val result = pointQueryService.getByUserLoginId(user.loginId)

            Then("포인트를 반환한다") {
                result shouldNotBe null
                result.amount shouldBe Money(1_000)
            }
        }
    }

    Given("해당 ID 의 회원이 존재하지 않는 경우") {
        val user = userJapRepository.save(createUser(LoginId("abc123")))
        pointJpaRepository.save(Point(user.id, Money(1_000)))

        When("포인트를 조회하면") {
            Then("예외를 반환한다") {
                shouldThrow<CoreException> { pointQueryService.getByUserLoginId(LoginId("xyz789")) }
            }
        }
    }
 })
