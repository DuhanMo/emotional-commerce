package com.loopers.infrastructure.product

import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductQueryResult
import com.loopers.domain.product.ProductRepository
import com.loopers.domain.product.ProductSummary
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository,
) : ProductRepository {
    override fun findProducts(sortBy: String, pageable: Pageable): List<ProductQueryResult> =
        productJpaRepository.findPage(pageable) {
            selectNew<ProductQueryResult>(
                entity(Product::class),
                entity(ProductSummary::class)
            ).from(
                entity(Product::class),
                leftJoin(entity(ProductSummary::class)).on(path(Product::id).equal(path(ProductSummary::productId)))
            ).orderBy(
                when (sortBy) {
                    "likes_desc" -> path(ProductSummary::likeCount).desc()
                    "price_asc" -> path(Product::price).asc()
                    else -> path(Product::id).desc()
                }
            )
        }.filterNotNull()
}