package com.loopers.domain.product

import com.loopers.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Version

@Table(name = "product_summary")
@Entity
class ProductSummary(
    val productId: Long,
    var likeCount: Long = 0,
) : BaseEntity() {
    @Version
    var version: Long = 0L

    fun increaseLikeCount() {
        this.likeCount += 1
    }

    fun decreaseLikeCount() {
        require(likeCount > 0) { "좋아요 수는 음수가 될 수 없습니다." }
        this.likeCount -= 1
    }

    fun updateLikeCount(likeCount: Long) {
        this.likeCount = likeCount
    }
}
