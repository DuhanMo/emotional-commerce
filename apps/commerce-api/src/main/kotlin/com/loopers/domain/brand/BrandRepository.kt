package com.loopers.domain.brand

interface BrandRepository {
    fun findAllByIds(brandIds: List<Long>): List<Brand>
}
