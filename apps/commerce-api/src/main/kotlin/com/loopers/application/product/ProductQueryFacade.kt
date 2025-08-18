package com.loopers.application.product

import com.loopers.domain.brand.BrandQueryService
import com.loopers.domain.product.ProductLikeQueryService
import com.loopers.domain.product.ProductQueryService
import com.loopers.domain.support.PageCriteria
import com.loopers.domain.support.cache.CacheNamespaces
import com.loopers.domain.support.cache.CachePolicy
import com.loopers.domain.support.cache.CacheTemplate
import com.loopers.domain.support.cache.KeyBuilder
import com.loopers.domain.support.cache.typeRef
import com.loopers.domain.user.LoginId
import com.loopers.domain.user.UserQueryService
import java.time.Duration
import org.springframework.stereotype.Service

@Service
class ProductQueryFacade(
    private val productQueryService: ProductQueryService,
    private val productLikeQueryService: ProductLikeQueryService,
    private val brandQueryService: BrandQueryService,
    private val userQueryService: UserQueryService,
    private val cacheTemplate: CacheTemplate,
) {
    fun findProducts(
        brandId: Long?,
        sortBy: String,
        pageCriteria: PageCriteria,
    ): ProductListOutput {
        val key = KeyBuilder.build(
            CacheNamespaces.PRODUCT_LIST,
            brandId ?: "ALL",
            sortBy,
            pageCriteria.page,
            pageCriteria.size,
            version = "1",
        )

        val policy = CachePolicy(
            ttl = Duration.ofMinutes(2),
            cacheNullAbsent = false,
        )
        return cacheTemplate.findOrLoad(
            key = key,
            type = typeRef<ProductListOutput>(),
            policy = policy,
        ) {
            val productPage = productQueryService.findAllProduct(
                brandId = brandId,
                sortBy = sortBy,
                pageCriteria = pageCriteria,
            )

            val brandIds = productPage.content.map { it.brandId }.distinct()
            val brands = brandQueryService.findBrands(brandIds)
            ProductListOutput.from(productPage, brands)
        }!!
    }

    fun get(
        productId: Long,
    ): ProductItemOutput {
        val key = KeyBuilder.build(
            CacheNamespaces.PRODUCT_DETAIL,
            productId,
            version = "1",
        )
        val policy = CachePolicy(
            ttl = Duration.ofMinutes(2),
            cacheNullAbsent = false,
        )
        return cacheTemplate.findOrLoad(
            key = key,
            type = typeRef<ProductItemOutput>(),
            policy = policy,
        ) {
            val productInfos = productQueryService.getByIdWithSummary(productId)
            val brand = brandQueryService.getById(productInfos.product.brandId)
            ProductItemOutput.from(productInfos, brand)
        }!!
    }

    fun findLikedProducts(
        loginId: LoginId,
    ): List<ProductItemOutput> {
        val user = userQueryService.getByLoginId(loginId)
        val productLikes = productLikeQueryService.findLikedProducts(user.id)

        val productIds = productLikes.map { it.productId }
        val productSummaries = productQueryService.findAllProductSummaryById(productIds)

        val brandIds = productSummaries.map { it.product.brandId }.distinct()
        val brands = brandQueryService.findBrands(brandIds).associateBy { it.id }

        return productSummaries.map { info ->
            val brand = brands[info.product.brandId]
                ?: throw IllegalStateException("해당 상품의 브랜드를 찾을 수 없습니다.(productId: ${info.product.id})")
            ProductItemOutput.from(info, brand)
        }
    }
}
