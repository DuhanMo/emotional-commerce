package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "product")
@Entity
class Product(
    val brandId: Long,
    val name: String,
    val description: String,
    val price: Int,
    var stock: Int,
    val imageUrl: String? = null,
) : BaseEntity() {
    fun deductStock(quantity: Int) {
        validateStock(quantity)
        this.stock -= quantity
    }

    private fun validateStock(requestedQuantity: Int) {
        require(requestedQuantity > 0) { "요청 수량은 0보다 커야 합니다." }
        require(this.stock >= requestedQuantity) {
            "상품 '${this.name}'의 재고가 부족합니다. (요청: $requestedQuantity, 재고: $stock)"
        }
    }
}
