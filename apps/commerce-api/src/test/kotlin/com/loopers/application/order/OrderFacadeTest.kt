package com.loopers.application.order

import com.loopers.domain.common.events.DomainEventPublisher
import com.loopers.domain.common.events.OrderCreatedEvent
import com.loopers.domain.coupon.CouponQueryService
import com.loopers.domain.order.OrderService
import com.loopers.domain.support.Money
import com.loopers.domain.user.UserQueryService
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import com.loopers.support.fixture.TEST_ADDRESS
import com.loopers.support.fixture.TEST_USER_ID
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
    val eventPublisher = mockk<DomainEventPublisher>()

    val facade = OrderFacade(
        userQueryService,
        couponQueryService,
        orderService,
        eventPublisher,
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
        every { eventPublisher.publish(any()) } just runs

        When("주문을 생성하면") {
            facade.placeOrder(baseInput)

            Then("주문 생성 이벤트를 발행한다") {
                verify(exactly = 1) { eventPublisher.publish(any<OrderCreatedEvent>()) }
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
