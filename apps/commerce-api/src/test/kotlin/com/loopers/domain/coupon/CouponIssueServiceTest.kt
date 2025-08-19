package com.loopers.domain.coupon

import com.loopers.support.fixture.createCoupon
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs

class CouponIssueServiceTest : BehaviorSpec({
    val couponRepository = mockk<CouponRepository>()
    val issuedCouponRepository = mockk<IssuedCouponRepository>()
    val service = CouponIssueService(couponRepository, issuedCouponRepository)

    Given("최대 발급 쿠폰보다 발급한 쿠폰이 적은 경우") {
        val coupon = createCoupon(maxIssueCount = 500L, issuedCount = 400L)
        every { couponRepository.getByIdWithLock(1L) } returns coupon
        every { couponRepository.save(any()) } returns coupon
        every { issuedCouponRepository.save(any())} returns IssuedCoupon(1L, coupon.id)

        When("쿠폰을 발급하면") {
            service.issue(1L, 1L)

            Then("발급 쿠폰이 생성된다") {
                coupon.issuedCount shouldBe 401L
            }
        }
    }

    afterTest {
        clearAllMocks()
    }
})
