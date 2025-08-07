package com.loopers.infrastructure.product

import com.loopers.domain.product.ProductLike
import com.loopers.domain.product.ProductLikeCount
import com.loopers.domain.product.ProductLikeRepository
import com.loopers.domain.product.ProductLikeStatus.ACTIVE
import java.time.ZonedDateTime
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ProductLikeRepositoryImpl(
    private val productLikeJpaRepository: ProductLikeJpaRepository,
) : ProductLikeRepository {
    override fun findByProductIdAndUserId(
        productId: Long,
        userId: Long,
    ): ProductLike? = productLikeJpaRepository.findByProductIdAndUserId(productId, userId)

    override fun save(productLike: ProductLike): ProductLike =
        productLikeJpaRepository.save(productLike)

    override fun findAllByUserId(userId: Long): List<ProductLike> =
        productLikeJpaRepository.findAllByUserIdAndStatusOrderByIdDesc(userId, ACTIVE)

    override fun findAllLikeUpdatedProductId(
        from: ZonedDateTime,
        to: ZonedDateTime,
        pageable: Pageable,
    ): Page<Long> = productLikeJpaRepository.findLikeUpdatedProductIds(from, to, pageable)

    override fun findAllActiveCountByProductId(productIds: List<Long>): List<ProductLikeCount> {
        if (productIds.isEmpty()) return emptyList()

        return productLikeJpaRepository.findAll {
            selectNew<ProductLikeCount>(
                path(ProductLike::productId),
                count(ProductLike::id),
            ).from(
                entity(ProductLike::class),
            ).where(
                path(ProductLike::productId).`in`(productIds)
                    .and(path(ProductLike::status).equal(ACTIVE)),
            ).groupBy(
                path(ProductLike::productId),
            )
        }.filterNotNull()
    }
}
