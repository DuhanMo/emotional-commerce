package com.loopers.domain.payment

enum class PaymentStatus {
    REQUESTED, // 결제 요청
    COMPLETED, // 결제 완료
    FAILED, // 결제 실패
    CANCELLED, // 결제 취소
}
