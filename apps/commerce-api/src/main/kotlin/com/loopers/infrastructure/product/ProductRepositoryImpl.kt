package com.loopers.infrastructure.product

import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductQueryResult
import com.loopers.domain.product.ProductRepository
import com.loopers.domain.product.ProductSummary
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository,
) : ProductRepository {
    override fun findProducts(
        brandId: Long?,
        sortBy: String,
        pageable: Pageable,
    ): Page<ProductQueryResult> {
        val page = productJpaRepository.findPage(pageable) {
            selectNew<ProductQueryResult>(
                entity(Product::class),
                entity(ProductSummary::class),
            ).from(
                entity(Product::class),
                leftJoin(entity(ProductSummary::class)).on(path(Product::id).equal(path(ProductSummary::productId))),
            ).apply {
                brandId?.let { where(path(Product::brandId).equal(it)) }
            }.orderBy(
                when (sortBy) {
                    "likes_desc" -> path(ProductSummary::likeCount).desc()
                    "price_asc" -> path(Product::price).asc()
                    else -> path(Product::id).desc()
                },
            )
        }

        val filteredContent = page.content.filterNotNull()
        return PageImpl(filteredContent, pageable, page.totalElements)
    }
}
