package com.loopers.domain.order

enum class OrderStatus {
    PENDING, // 초기 상태 (주문 생성)
    PAID, // 결제 완료
    PAYMENT_FAILED, // 결제 실패
}
