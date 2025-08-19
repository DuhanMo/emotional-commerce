package com.loopers.domain.support

@JvmInline
value class Money(val value: Long) {

    init {
        require(value >= 0L) { "금액은 음수가 될 수 없습니다." }
    }

    operator fun plus(other: Money): Money =
        Money(this.value + other.value)

    operator fun minus(other: Money): Money =
        Money(this.value - other.value)

    operator fun times(multiplier: Long): Money =
        Money(this.value * multiplier)

    operator fun compareTo(other: Money): Int =
        this.value.compareTo(other.value)

    override fun toString(): String = value.toString()

    companion object {
        val ZERO: Money = Money(0)
    }
}
