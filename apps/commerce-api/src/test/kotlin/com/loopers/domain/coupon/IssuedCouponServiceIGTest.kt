package com.loopers.domain.coupon

import com.loopers.infrastructure.coupon.IssuedCouponJpaRepository
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class IssuedCouponServiceIGTest(
    private val issuedCouponService: IssuedCouponService,
    private val issuedCouponJpaRepository: IssuedCouponJpaRepository,
) : IntegrationSpec({
    Given("동시에 같은 발급 쿠폰을 사용하려는 경우") {
        val issuedCoupon = issuedCouponJpaRepository.save(IssuedCoupon(userId = 1L, couponId = 1L))

        When("사용 요청하면") {
            val results = (0 until 2).map { coroutineIndex ->
                async(Dispatchers.IO) {
                    runCatching {
                        issuedCouponService.useCoupon(1L, issuedCoupon.id)
                    }
                }
            }.awaitAll()

            Then("하나의 요청은 실패한다") {
                val foundIssuedCoupon = issuedCouponJpaRepository.findAll().first()
                foundIssuedCoupon.status shouldBe IssuedCouponStatus.USED
                results.count { it.isSuccess } shouldBe 1
                results.count { it.isFailure } shouldBe 1
            }
        }
    }
})
