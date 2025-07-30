package com.loopers.domain.brand

import org.springframework.stereotype.Service

@Service
class BrandQueryService(
    private val brandRepository: BrandRepository,
) {
    fun findBrands(brandIds: List<Long>): List<Brand> = brandRepository.findAllByIds(brandIds)
}
