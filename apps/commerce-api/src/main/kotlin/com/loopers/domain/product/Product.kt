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
    var likeCount: Long = 0,
    val imageUrl: String? = null,
    id: Long = 0L,
) : BaseEntity(id) {
    fun deductStock(quantity: Int) {
        validateStock(quantity)
        this.stock -= quantity
    }

    fun increaseLikeCount() {
        likeCount++
    }

    fun decreaseLikeCount() {
        check(likeCount > 0) { "좋아요 수는 0 미만일 수 없습니다." }
        likeCount--
    }

    private fun validateStock(requestedQuantity: Int) {
        require(requestedQuantity > 0) { "요청 수량은 0보다 커야 합니다." }
        require(stock >= requestedQuantity) {
            "상품 '$name'의 재고가 부족합니다. (요청: $requestedQuantity, 재고: $stock)"
        }
    }
}
