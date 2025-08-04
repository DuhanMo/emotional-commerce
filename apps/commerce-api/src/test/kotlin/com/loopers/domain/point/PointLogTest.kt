package com.loopers.domain.point

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.longs.shouldBeGreaterThan

class PointLogTest : StringSpec({
    "사용 포인트 로그를 생성하면 amount가 음수이다" {
        val usePointLog = PointLog.fromUse(1L, 1L, 1000L)

        usePointLog.amount shouldBeLessThan 0
    }

    "충전 포인트 로그를 생성하면 amount가 양수이다" {
        val usePointLog = PointLog.fromCharge(1L, 1L, 1000L)

        usePointLog.amount shouldBeGreaterThan 0
    }
})
