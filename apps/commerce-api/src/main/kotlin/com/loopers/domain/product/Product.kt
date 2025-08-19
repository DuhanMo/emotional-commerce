package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "product")
@Entity
class Product(
    val brandId: Long,
    val name: String,
    val description: String,
    @Enumerated(EnumType.STRING)
    val status: ProductStatus,
    val price: Int,
    var stock: Int,
    var likeCount: Long = 0,
    val imageUrl: String? = null,
    id: Long = 0L,
) : BaseEntity(id) {

    fun increaseLikeCount() {
        likeCount++
    }

    fun decreaseLikeCount() {
        check(likeCount > 0) { "좋아요 수는 0 미만일 수 없습니다." }
        likeCount--
    }

    enum class ProductStatus {
        ACTIVE,
        INACTIVE,
    }
}
