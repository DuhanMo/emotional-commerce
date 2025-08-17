package com.loopers.application.point

import com.loopers.domain.point.Point
import com.loopers.domain.user.LoginId
import com.loopers.infrastructure.point.PointHistoryJpaRepository
import com.loopers.infrastructure.point.PointJpaRepository
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.support.error.CoreException
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.DciSpec
import com.loopers.support.tests.IntegrationTest
import com.loopers.utils.DatabaseCleanUp
import com.loopers.utils.RedisCleanUp
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.data.repository.findByIdOrNull

class PointFacadeIGTest(
    private val pointFacade: PointFacade,
    private val userJpaRepository: UserJpaRepository,
    private val pointJpaRepository: PointJpaRepository,
    private val pointHistoryJpaRepository: PointHistoryJpaRepository,
    private var databaseCleanUp: DatabaseCleanUp,
    private var redisCleanUp: RedisCleanUp,
) : DciSpec({
    afterEach {
        databaseCleanUp.truncateAllTables()
        redisCleanUp.truncateAll()
    }

    describe("PointFacade.charge") {
        context("로그인 ID가 존재하지 않는 경우") {
            val user = userJpaRepository.save(createUser(loginId = LoginId("abc123")))
            pointJpaRepository.save(Point(user.id))

            it("예외 발생한다") {
                shouldThrow<CoreException> {
                    pointFacade.charge(ChargePointInput(LoginId("xyz789"), 100))
                }
            }
        }

        context("로그인 ID가 존재하는 경우") {
            val user = userJpaRepository.save(createUser(loginId = LoginId("abc123")))
            val point = pointJpaRepository.save(Point(user.id))

            it("포인트가 증가하고 포인트 로그가 적재된다") {
                pointFacade.charge(ChargePointInput(LoginId("abc123"), 100))
                val foundPoint = pointJpaRepository.findByIdOrNull(point.id)!!
                foundPoint.amount shouldBe 100
                pointHistoryJpaRepository.findAll().first { it.pointId == foundPoint.id }.amount shouldBe 100
            }
        }
    }
})
