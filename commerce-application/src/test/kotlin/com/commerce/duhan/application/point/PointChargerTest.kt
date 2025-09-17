package com.commerce.duhan.application.point

import com.commerce.duhan.domain.fixtures.createPoint
import com.commerce.duhan.domain.fixtures.createPointHistory
import com.commerce.duhan.domain.point.PointHistoryRepository
import com.commerce.duhan.domain.point.PointRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class PointChargerTest : DescribeSpec({
    val pointRepository = mockk<PointRepository>()
    val pointHistoryRepository = mockk<PointHistoryRepository>()
    val underTest = PointCharger(pointRepository, pointHistoryRepository)

    describe("포인트 충전") {
        context("포인트가 존재하지 않는 경우") {
            it("예외가 발생한다") {
                // given
                every { pointRepository.getByUserIdWithLock(any()) } throws NoSuchElementException()

                // when & then
                shouldThrow<NoSuchElementException> {
                    underTest.charge(userId = 1L, amount = 1000)
                }
            }
        }

        context("포인트가 존재하는 경우 경우") {
            it("포인트가 증가한다") {
                // given
                val point = createPoint(amount = 1000)
                every { pointRepository.getByUserIdWithLock(any()) } returns point
                every { pointHistoryRepository.save(any()) } returns createPointHistory()

                // when
                underTest.charge(userId = 1L, amount = 900)

                // then
                point.amount shouldBe 1900
            }
            it("포인트 히스토리가 남는다") {
                // given
                val point = createPoint(amount = 1000)
                every { pointRepository.getByUserIdWithLock(any()) } returns point
                every { pointHistoryRepository.save(any()) } returns createPointHistory()

                // when
                underTest.charge(userId = 1L, amount = 900)

                // then
                verify(exactly = 1) { pointHistoryRepository.save(any()) }
            }
        }
    }

    afterTest {
        clearAllMocks()
    }
})
