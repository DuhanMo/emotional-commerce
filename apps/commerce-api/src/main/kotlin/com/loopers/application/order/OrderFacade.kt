package com.loopers.application.order

import com.loopers.domain.coupon.Coupon
import com.loopers.domain.coupon.CouponQueryService
import com.loopers.domain.coupon.IssuedCouponService
import com.loopers.domain.order.Address
import com.loopers.domain.order.CreateOrderCommand
import com.loopers.domain.order.OrderInfo.OrderLineInfo
import com.loopers.domain.order.OrderService
import com.loopers.domain.payment.PaymentService
import com.loopers.domain.product.ProductService
import com.loopers.domain.user.User
import com.loopers.domain.user.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderFacade(
    private val userQueryService: UserQueryService,
    private val couponQueryService: CouponQueryService,
    private val orderService: OrderService,
    private val paymentService: PaymentService,
    private val productService: ProductService,
    private val issuedCouponService: IssuedCouponService,
) {
    @Transactional
    fun placeOrder(input: PlaceOrderInput): PlaceOrderOutput {
        val user = userQueryService.getByLoginId(input.loginId)
        val coupon = couponQueryService.findByIssuedCouponId(input.issuedCouponId)
        val createOrderCommand = toCreateOrderCommand(user, input, coupon)
        val order = orderService.createOrder(createOrderCommand)
        paymentService.pay(user, order)
        input.issuedCouponId?.let { issuedCouponService.useCoupon(user.id, it) }
        productService.deductStock(order)
        return PlaceOrderOutput.from(order)
    }

    private fun toCreateOrderCommand(
        user: User,
        input: PlaceOrderInput,
        coupon: Coupon?,
    ): CreateOrderCommand = CreateOrderCommand(
        userId = user.id,
        deliveryAddress = Address(
            street = input.address.street,
            city = input.address.city,
            zipCode = input.address.zipCode,
            detailAddress = input.address.detailAddress,
        ),
        payMethod = input.payMethod,
        orderLines = input.orderItems.map {
            OrderLineInfo(
                productId = it.productId,
                quantity = it.quantity,
                unitPrice = it.unitPrice,
            )
        },
        coupon = coupon,
    )
}
