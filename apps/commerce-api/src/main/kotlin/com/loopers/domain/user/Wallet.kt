package com.loopers.domain.user

class Wallet(
    val user: User,
    var point: Int = 0,
) {
    fun charge(point: Int) {
        require(point > 0) { "포인트는 0보다 커야 합니다." }
        this.point += point
    }
}
