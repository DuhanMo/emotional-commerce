package com.commerce.duhan.db.product

import com.commerce.duhan.domain.product.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<Product, Long>
