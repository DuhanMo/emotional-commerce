package com.loopers.application.product

import com.loopers.domain.product.ProductLikeService
import com.loopers.domain.product.ProductSummaryService
import com.loopers.domain.user.UserQueryService
import org.springframework.stereotype.Service

@Service
class ProductFacade(
    private val userQueryService: UserQueryService,
    private val productLikeService: ProductLikeService,
    private val productSummaryService: ProductSummaryService,
) {
    fun likeProduct(input: LikeProductInput) {
        val user = userQueryService.getByLoginId(input.loginId)
        val productLike = productLikeService.likeProduct(productId = input.productId, userId = user.id)
        productSummaryService.increaseLikeCount(productLike)
    }

    fun unlikeProduct(input: UnlikeProductInput) {
        val user = userQueryService.getByLoginId(input.loginId)
        val productLike = productLikeService.unlikeProduct(productId = input.productId, userId = user.id)
        productSummaryService.decreaseLikeCount(productLike)
    }
}
