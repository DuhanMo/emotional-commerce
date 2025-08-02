package com.loopers.infrastructure.product

import com.loopers.domain.product.ProductSummary
import com.loopers.domain.product.ProductSummaryRepository
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.stereotype.Repository

@Repository
class ProductSummaryRepositoryImpl(
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
) : ProductSummaryRepository {
    override fun getByProductId(productId: Long): ProductSummary =
        productSummaryJpaRepository.findByProductId(productId)
            ?: throw CoreException(ErrorType.NOT_FOUND, "상품집계를 찾을 수 없습니다.(productId: $productId)")

    override fun save(productSummary: ProductSummary): ProductSummary =
        productSummaryJpaRepository.save(productSummary)
}
