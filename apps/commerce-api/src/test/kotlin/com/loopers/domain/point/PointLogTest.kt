package com.loopers.domain.point

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.longs.shouldBeGreaterThan

class PointLogTest : StringSpec({
    "사용 포인트 로그를 생성하면 amount가 음수이다" {
        val usePointHistory = PointHistory.fromUse(1L, 1L, 1000L)

        usePointHistory.amount shouldBeLessThan 0
    }

    "충전 포인트 로그를 생성하면 amount가 양수이다" {
        val usePointHistory = PointHistory.fromCharge(1L, 1L, 1000L)

        usePointHistory.amount shouldBeGreaterThan 0
    }
})
