package com.loopers.infrastructure.product

import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductQueryResult
import com.loopers.domain.product.ProductRepository
import com.loopers.domain.product.ProductSummary
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository,
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
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
                join(entity(ProductSummary::class)).on(path(Product::id).equal(path(ProductSummary::productId))),
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

    override fun getById(productId: Long): ProductQueryResult {
        val product = productJpaRepository.findByIdOrNull(productId)
            ?: throw CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다.(productId: $productId)")
        val summary = productSummaryJpaRepository.findByProductId(product.id)
            ?: throw CoreException(ErrorType.NOT_FOUND, "상품집계를 찾을 수 없습니다.(productId: $productId)")

        return ProductQueryResult(product, summary)
    }
}
