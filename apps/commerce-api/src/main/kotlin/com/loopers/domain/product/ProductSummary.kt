package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "product_summary")
@Entity
class ProductSummary(
    val productId: Long,
    var likeCount: Long = 0,
) : BaseEntity() {
    fun increaseLikeCount() {
        this.likeCount += 1
    }

    fun decreaseLikeCount() {
        require(likeCount > 0) { "좋아요 수는 음수가 될 수 없습니다." }
        this.likeCount -= 1
    }
}
