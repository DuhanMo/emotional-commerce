package com.loopers.application.point

import com.loopers.domain.point.ChargePointCommand
import com.loopers.domain.point.Point
import com.loopers.domain.user.LoginId
import com.loopers.infrastructure.user.PointJpaRepository
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.support.error.CoreException
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.IntegrationTest
import com.loopers.utils.DatabaseCleanUp
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.springframework.data.repository.findByIdOrNull

@IntegrationTest
class PointServiceIGTest(
    private val pointService: PointService,
    private val databaseCleanUp: DatabaseCleanUp,
    private val userJpaRepository: UserJpaRepository,
    private val pointJpaRepository: PointJpaRepository,
) : BehaviorSpec({
    afterEach {
        databaseCleanUp.truncateAllTables()
    }

    Given("로그인 ID가 존재하지 않는 경우") {
        val user = userJpaRepository.save(createUser(loginId = LoginId("abc123")))
        pointJpaRepository.save(Point(user))

        When("포인트를 충전하면") {
            Then("예외 발생한다") {
                shouldThrow<CoreException> {
                    pointService.charge(ChargePointCommand(LoginId("xyz789"), 100))
                }
            }
        }
    }

    Given("로그인 ID가 존재하는 경우") {
        val user = userJpaRepository.save(createUser(loginId = LoginId("abc123")))
        val point = pointJpaRepository.save(Point(user))

        When("포인트를 충전하면") {
            pointService.charge(ChargePointCommand(LoginId("abc123"), 100))

            Then("포인트가 증가한다") {
                val foundPoint = pointJpaRepository.findByIdOrNull(point.id)!!
                foundPoint.amount shouldBe 100
            }
        }
    }
})