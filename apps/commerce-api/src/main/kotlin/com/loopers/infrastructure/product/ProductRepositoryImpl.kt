package com.loopers.infrastructure.product

import com.linecorp.kotlinjdsl.dsl.jpql.Jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.expression.Expressions.count
import com.linecorp.kotlinjdsl.querymodel.jpql.path.Paths.path
import com.linecorp.kotlinjdsl.querymodel.jpql.predicate.Predicate
import com.linecorp.kotlinjdsl.querymodel.jpql.sort.Sortable
import com.loopers.domain.product.Product
import com.loopers.domain.product.ProductLike
import com.loopers.domain.product.ProductLikeStatus.ACTIVE
import com.loopers.domain.product.ProductRepository
import com.loopers.domain.product.ProductSummary
import com.loopers.domain.product.ProductWithLikeCount
import com.loopers.domain.product.ProductWithSummaryInfo
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
    override fun findAllProductSummary(
        brandId: Long?,
        sortBy: String,
        pageable: Pageable,
    ): Page<ProductWithSummaryInfo> {
        val page = productJpaRepository.findPage(pageable) {
            selectNew<ProductWithSummaryInfo>(
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

    override fun findAllProductWithLikeCount(
        brandId: Long?,
        sortBy: String,
        pageable: Pageable,
    ): Page<ProductWithLikeCount> {
        val page = productJpaRepository.findPage(pageable) {
            selectNew<ProductWithLikeCount>(
                entity(Product::class),
                count(ProductLike::id),
            ).from(
                entity(Product::class),
                leftJoin(entity(ProductLike::class))
                    .on(
                        path(Product::id).equal(path(ProductLike::productId))
                            .and(path(ProductLike::status).eq(ACTIVE)),
                    ),
            ).where(
                eqBrandId(brandId),
            ).groupBy(
                path(Product::id),
            ).orderBy(
                *makeSort(sortBy),
            )
        }
        val filteredContent = page.content.filterNotNull()
        return PageImpl(filteredContent, pageable, page.totalElements)
    }

    private fun Jpql.makeSort(sortBy: String): Array<out Sortable?> = when (sortBy) {
        "likes_desc" -> arrayOf(count(ProductLike::id).desc(), path(Product::id).desc())
        "price_asc" -> arrayOf(path(Product::price).asc(), path(Product::id).desc())
        else -> arrayOf(path(Product::id).desc())
    }

    private fun Jpql.eqBrandId(brandId: Long?): Predicate? = brandId?.let { (path(Product::brandId).equal(it)) }

    override fun getById(id: Long): ProductWithSummaryInfo {
        val product = productJpaRepository.findByIdOrNull(id)
            ?: throw CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다.(productId: $id)")
        val summary = productSummaryJpaRepository.findByProductId(product.id)
            ?: throw CoreException(ErrorType.NOT_FOUND, "상품집계를 찾을 수 없습니다.(productId: $id)")

        return ProductWithSummaryInfo(product, summary)
    }

    override fun findAllProductSummaryById(ids: List<Long>): List<ProductWithSummaryInfo> =
        productJpaRepository.findAll {
            selectNew<ProductWithSummaryInfo>(
                entity(Product::class),
                entity(ProductSummary::class),
            ).from(
                entity(Product::class),
                join(entity(ProductSummary::class)).on(path(Product::id).equal(path(ProductSummary::productId))),
            ).where(
                path(Product::id).`in`(ids),
            )
        }.filterNotNull()

    override fun findAllById(ids: List<Long>): List<Product> = productJpaRepository.findAllById(ids)

    override fun save(product: Product): Product = productJpaRepository.save(product)

    override fun getByIdWithLock(id: Long): Product = productJpaRepository.findByIdWithLock(id)
            ?: throw CoreException(ErrorType.NOT_FOUND, "상품을 찾을 수 없습니다.(productId: $id)")
}
