package com.loopers.application.point

import com.loopers.domain.point.ChargePointCommand
import com.loopers.domain.point.Point
import com.loopers.domain.user.LoginId
import com.loopers.infrastructure.point.PointJpaRepository
import com.loopers.infrastructure.point.PointLogJpaRepository
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.support.error.CoreException
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.IntegrationSpec
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.springframework.data.repository.findByIdOrNull

class PointServiceIGTest(
    private val pointService: PointService,
    private val userJpaRepository: UserJpaRepository,
    private val pointJpaRepository: PointJpaRepository,
    private val pointLogJpaRepository: PointLogJpaRepository,
) : IntegrationSpec({
    Given("로그인 ID가 존재하지 않는 경우") {
        val user = userJpaRepository.save(createUser(loginId = LoginId("abc123")))
        pointJpaRepository.save(Point(user.id))

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
        val point = pointJpaRepository.save(Point(user.id))

        When("포인트를 충전하면") {
            pointService.charge(ChargePointCommand(LoginId("abc123"), 100))

            Then("포인트가 증가하고 포인트 로그가 적재된다") {
                val foundPoint = pointJpaRepository.findByIdOrNull(point.id)!!
                foundPoint.amount shouldBe 100
                pointLogJpaRepository.findAll().first { it.pointId == foundPoint.id }.amount shouldBe 100
            }
        }
    }
})
