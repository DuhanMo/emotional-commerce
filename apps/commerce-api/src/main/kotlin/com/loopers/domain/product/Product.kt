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
    fun deductStock(stock: Int) {
        require(stock <= this.stock) { "재고를 초과하여 재고 차감할 수 없습니다." }
        this.stock -= stock
    }
}
