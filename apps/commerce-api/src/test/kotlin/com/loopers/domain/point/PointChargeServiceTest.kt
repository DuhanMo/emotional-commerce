package com.loopers.domain.point

import com.loopers.domain.support.Money
import com.loopers.support.fixture.createPoint
import com.loopers.support.fixture.createPointHistory
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class PointChargeServiceTest : BehaviorSpec({
    val pointRepository = mockk<PointRepository>()
    val pointHistoryRepository = mockk<PointHistoryRepository>()
    val service = PointChargeService(pointRepository, pointHistoryRepository)

    Given("포인트 충전요청이 정상인 경우") {
        every { pointRepository.save(any()) } returns createPoint()
        every { pointHistoryRepository.save(any()) } returns createPointHistory()

        When("포인트를 충전하면") {
            service.charge(createPoint(id = 99L, userId = 1L), Money(100))

            Then("포인트 히스토리가 저장된다") {
                verify { pointHistoryRepository.save(any()) }
            }
        }
    }

    afterTest {
        clearAllMocks()
    }
})
