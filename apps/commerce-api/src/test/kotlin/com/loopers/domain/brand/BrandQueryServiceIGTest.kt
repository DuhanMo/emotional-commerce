package com.loopers.domain.brand

import com.loopers.infrastructure.brand.BrandJpaRepository
import com.loopers.support.tests.IntegrationSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class BrandQueryServiceIGTest(
    private val brandQueryService: BrandQueryService,
    private val brandJpaRepository: BrandJpaRepository,
) : IntegrationSpec({
    Given("브랜드가 존재하는 경우") {
        val brand1 = brandJpaRepository.save(Brand("브랜드1", "브랜드 설명1"))
        val brand2 = brandJpaRepository.save(Brand("브랜드2", "브랜드 설명2"))
        val brand3 = brandJpaRepository.save(Brand("브랜드3", "브랜드 설명3"))

        When("브랜드 ID 목록으로 조회하면") {
            val brandIds = listOf(brand1.id, brand2.id)
            val result = brandQueryService.findBrands(brandIds)

            Then("해당 브랜드들을 반환한다") {
                result shouldHaveSize 2
                result.map { it.id } shouldBe listOf(brand1.id, brand2.id)
                result.map { it.name } shouldBe listOf("브랜드1", "브랜드2")
            }
        }
    }

    Given("존재하지 않는 브랜드 ID가 포함된 경우") {
        val brand1 = brandJpaRepository.save(Brand("Brand1", "브랜드 설명1"))

        When("존재하지 않는 ID와 함께 조회하면") {
            val brandIds = listOf(brand1.id, 999L)
            val result = brandQueryService.findBrands(brandIds)

            Then("존재하는 브랜드만 반환한다") {
                result shouldHaveSize 1
                result.first().id shouldBe brand1.id
                result.first().name shouldBe "Brand1"
            }
        }
    }

    Given("빈 ID 목록인 경우") {
        When("빈 목록으로 조회하면") {
            val result = brandQueryService.findBrands(emptyList())

            Then("빈 목록을 반환한다") {
                result shouldHaveSize 0
            }
        }
    }
})
