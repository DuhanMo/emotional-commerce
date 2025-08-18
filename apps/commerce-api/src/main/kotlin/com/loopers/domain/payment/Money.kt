package com.loopers.domain.payment

import java.math.BigDecimal

@JvmInline
value class Money(private val amount: BigDecimal) {

    init {
        require(amount >= BigDecimal.ZERO) {
            "금액은 음수가 될 수 없습니다."
        }
    }

    operator fun plus(other: Money): Money =
        Money(this.amount + other.amount)

    operator fun minus(other: Money): Money =
        Money(this.amount - other.amount)

    operator fun times(multiplier: BigDecimal): Money =
        Money(this.amount.multiply(multiplier))

    operator fun compareTo(other: Money): Int =
        this.amount.compareTo(other.amount)

    fun toBigDecimal(): BigDecimal = amount

    override fun toString(): String = amount.toPlainString()

    companion object {
        val ZERO: Money = Money(BigDecimal.ZERO)

        fun of(value: Long): Money = Money(BigDecimal.valueOf(value))
        fun of(value: String): Money = Money(BigDecimal(value))
    }
}