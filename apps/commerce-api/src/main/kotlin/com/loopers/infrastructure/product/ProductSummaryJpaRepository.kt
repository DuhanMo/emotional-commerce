package com.loopers.infrastructure.product

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.loopers.domain.product.ProductSummary
import org.springframework.data.jpa.repository.JpaRepository

interface ProductSummaryJpaRepository : JpaRepository<ProductSummary, Long>, KotlinJdslJpqlExecutor
