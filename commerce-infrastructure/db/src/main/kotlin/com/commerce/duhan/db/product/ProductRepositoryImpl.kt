package com.commerce.duhan.db.product

import com.commerce.duhan.domain.product.Product
import com.commerce.duhan.domain.product.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
    private val jpaRepository: ProductJpaRepository,
) : ProductRepository {
    override fun getByProductId(id: Long): Product = jpaRepository.findByIdOrNull(id)
        ?: throw NoSuchElementException("상품을 찾을 수 없습니다.(id: $id)")
}
