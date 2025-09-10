package com.loopers.application.order

import com.loopers.domain.coupon.CouponQueryService
import com.loopers.domain.coupon.IssuedCouponService
import com.loopers.domain.order.OrderService
import com.loopers.domain.product.InventoryService
import com.loopers.domain.support.Money
import com.loopers.domain.user.UserQueryService
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import com.loopers.support.fixture.TEST_ADDRESS
import com.loopers.support.fixture.TEST_USER_ID
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk

class OrderFacadeTest : BehaviorSpec(
    {
        val userQueryService = mockk<UserQueryService>()
        val couponQueryService = mockk<CouponQueryService>()
        val orderService = mockk<OrderService>()
        val inventoryService = mockk<InventoryService>()
        val issuedCouponService = mockk<IssuedCouponService>()

        val facade = OrderFacade(
            userQueryService,
            couponQueryService,
            orderService,
            inventoryService,
            issuedCouponService,
        )

        Given("유저가 존재하지 않는 경우") {
            every { userQueryService.getByLoginId(baseInput.loginId) } throws CoreException(
                ErrorType.NOT_FOUND,
                "존재하지 않는 회원입니다",
            )

            When("주문을 생성하면") {
                Then("예외 발생한다") {
                    shouldThrow<CoreException> { facade.placeOrder(baseInput) }
                }
            }
        }

        afterTest {
            clearAllMocks()
        }
    },
)

private val baseInput = PlaceOrderInput(
    loginId = TEST_USER_ID,
    orderItems = listOf(
        PlaceOrderInput.OrderLineInput(
            productId = 1L,
            skuId = 1L,
            quantity = 2L,
            unitPrice = Money(1000),
        ),
        PlaceOrderInput.OrderLineInput(
            productId = 2L,
            skuId = 2L,
            quantity = 1L,
            unitPrice = Money(2000),
        ),
    ),
    totalAmount = Money(4000),
    deliveryAddress = TEST_ADDRESS,
    issuedCouponId = null,
)
