package com.loopers.infrastructure.brand

import com.loopers.domain.brand.Brand
import com.loopers.domain.brand.BrandRepository
import com.loopers.support.error.CoreException
import com.loopers.support.error.ErrorType
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class BrandRepositoryImpl(
    private val brandJpaRepository: BrandJpaRepository,
) : BrandRepository {
    override fun findAllByIds(brandIds: List<Long>): List<Brand> = brandJpaRepository.findAllById(brandIds)

    override fun getById(brandId: Long): Brand = brandJpaRepository.findByIdOrNull(brandId)
        ?: throw CoreException(ErrorType.NOT_FOUND, "브랜드를 찾을 수 없습니다.(brandId: $brandId)")
}
