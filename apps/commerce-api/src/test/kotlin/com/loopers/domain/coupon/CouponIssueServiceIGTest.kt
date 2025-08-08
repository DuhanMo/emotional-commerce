package com.loopers.domain.coupon

import com.loopers.infrastructure.coupon.CouponJpaRepository
import com.loopers.infrastructure.coupon.IssuedCouponJpaRepository
import com.loopers.support.fixture.createCoupon
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.springframework.data.repository.findByIdOrNull

class CouponIssueServiceIGTest(
    private val couponIssueService: CouponIssueService,
    private val couponJpaRepository: CouponJpaRepository,
    private val issuedCouponJpaRepository: IssuedCouponJpaRepository,
) : IntegrationSpec({
    Given("쿠폰이 발급 가능할 경우") {
        val coupon = createCoupon()
        couponJpaRepository.save(coupon)

        When("쿠폰을 발급하면") {
            couponIssueService.issue(1L, coupon.id)

            Then("발급 쿠폰이 생성된다") {
                val issuedCoupon =
                    issuedCouponJpaRepository.findAll().find { it.userId == 1L && it.couponId == coupon.id }
                issuedCoupon shouldNotBe null
            }
        }
    }

    Given("쿠폰 재고보다 많은 요청이 존재하는 경우") {
        val coupon = createCoupon(maxIssueCount = 10L, issuedCount = 0L)
        couponJpaRepository.save(coupon)

        When("동시에 발급 요청하면") {
            // 30번 요청
            val results = (0 until 30).map { coroutineIndex ->
                async(Dispatchers.IO) {
                    runCatching {
                        couponIssueService.issue(coroutineIndex.toLong(), coupon.id)
                    }
                }
            }.awaitAll()

            Then("쿠폰 발급 수량은 재고와 동일하고 나머지 요청은 실패한다") {
                val foundCoupon = couponJpaRepository.findByIdOrNull(coupon.id)!!

                foundCoupon.maxIssueCount shouldBe foundCoupon.issuedCount
                issuedCouponJpaRepository.findAll().count() shouldBe 10
                results.count { it.isFailure } shouldBe 20
            }
        }
    }
})
