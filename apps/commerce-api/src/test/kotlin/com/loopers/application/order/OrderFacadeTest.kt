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
import com.loopers.support.fixture.createCoupon
import com.loopers.support.fixture.createOrder
import com.loopers.support.fixture.createUser
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify

class OrderFacadeTest : BehaviorSpec({
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

    Given("주문이 정상 생성되는 경우") {
        every { userQueryService.getByLoginId(baseInput.loginId) } returns createUser()
        every { orderService.createOrder(any()) } returns createOrder()
        every { inventoryService.reserveAll(any()) } just runs

        When("주문을 생성하면") {
            facade.placeOrder(baseInput)

            Then("재고를 선점을 시도한다") {
                verify(exactly = 1) { inventoryService.reserveAll(any()) }
            }
        }
    }

    Given("재고 선점에서 예외가 발생하는 경우") {
        every { userQueryService.getByLoginId(baseInput.loginId) } returns createUser()
        every { orderService.createOrder(any()) } returns createOrder()
        every { inventoryService.reserveAll(any()) } throws IllegalArgumentException("재고가 부족합니다.")

        When("주문을 생성하면") {
            Then("예외발생한다") {
                shouldThrow<IllegalArgumentException> { facade.placeOrder(baseInput) }
            }
        }
    }

    Given("발급쿠폰이 존재하는 경우") {
        val input = baseInput.copy(issuedCouponId = 1L)
        every { userQueryService.getByLoginId(baseInput.loginId) } returns createUser()
        every { couponQueryService.findByIssuedCouponId(any()) } returns createCoupon()
        every { orderService.createOrder(any()) } returns createOrder()
        every { inventoryService.reserveAll(any()) } just runs
        every { issuedCouponService.pendingCoupon(any(), any()) } just runs

        When("주문을 생성하면") {
            facade.placeOrder(input)

            Then("발급쿠폰을 보류 처리한다") {
                verify(exactly = 1) { issuedCouponService.pendingCoupon(any(), any()) }
            }
        }
    }

    afterTest {
        clearAllMocks()
    }
})

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
