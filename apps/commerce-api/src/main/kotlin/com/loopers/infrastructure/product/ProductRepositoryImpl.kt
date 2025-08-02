package com.loopers.infrastructure.product

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.Predicate
import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductInfo
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
    ): Page<ProductInfo> {
        val page = productJpaRepository.findPage(pageable) {
            selectNew<ProductInfo>(
                entity(Product::class),
                entity(ProductSummary::class),
            ).from(
                entity(Product::class),
                join(entity(ProductSummary::class)).on(path(Product::id).equal(path(ProductSummary::productId))),
            ).where(
                eqBrandId(brandId),
            ).orderBy(
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

    private fun Jpql.eqBrandId(brandId: Long?): Predicate? = brandId?.let { (path(Product::brandId).equal(it)) }

    override fun getById(productId: Long): ProductInfo {
        val product = productJpaRepository.findByIdOrNull(productId)
            ?: throw CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다.(productId: $productId)")
        val summary = productSummaryJpaRepository.findByProductId(product.id)
            ?: throw CoreException(ErrorType.NOT_FOUND, "상품집계를 찾을 수 없습니다.(productId: $productId)")

        return ProductInfo(product, summary)
    }

    override fun findAllById(productIds: List<Long>): List<ProductInfo> =
        productJpaRepository.findAll {
            selectNew<ProductInfo>(
                entity(Product::class),
                entity(ProductSummary::class),
            ).from(
                entity(Product::class),
                join(entity(ProductSummary::class)).on(path(Product::id).equal(path(ProductSummary::productId))),
            ).where(
                path(Product::id).`in`(productIds),
            )
        }.filterNotNull()

    override fun save(product: Product): Product = productJpaRepository.save(product)
}
