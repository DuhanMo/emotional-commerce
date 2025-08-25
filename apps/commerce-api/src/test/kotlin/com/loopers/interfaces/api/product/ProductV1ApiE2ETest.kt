 package com.loopers.interfaces.api.product

 import com.loopers.infrastructure.brand.BrandJpaRepository
 import com.loopers.infrastructure.product.ProductJpaRepository
 import com.loopers.infrastructure.product.ProductSummaryJpaRepository
 import com.loopers.support.tests.E2ESpec
 import org.springframework.boot.test.web.client.TestRestTemplate

 class ProductV1ApiE2ETest(
    private val brandJpaRepository: BrandJpaRepository,
    private val productJpaRepository: ProductJpaRepository,
    private val productSummaryJpaRepository: ProductSummaryJpaRepository,
    private val testRestTemplate: TestRestTemplate,
 ) : E2ESpec({
//    /**
//     * @see ProductV1Controller.findProducts
//     */
//    describe("GET /api/v1/products") {
//        val url = "/api/v1/products"
//
//        it("상품 목록이 존재하는 경우 - 상품 목록을 조회하면, 상품 정보와 브랜드 정보가 포함된 응답을 반환한다") {
//            // Given
//            val brand1 = brandJpaRepository.save(createBrand(name = "브랜드1", description = "브랜드1 설명"))
//            val brand2 = brandJpaRepository.save(createBrand(name = "브랜드2", description = "브랜드2 설명"))
//
//            productJpaRepository.save(
//                createProduct(
//                    brandId = brand1.id,
//                    name = "상품1",
//                    description = "상품1 설명",
//                    price = 10_000,
//                    stock = 50,
//                    likeCount = 100,
//                ),
//            )
//            productJpaRepository.save(
//                createProduct(
//                    brandId = brand2.id,
//                    name = "상품2",
//                    description = "상품2 설명",
//                    price = 20_000,
//                    stock = 30,
//                    likeCount = 200,
//                ),
//            )
//
//            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductListResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType)
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.data?.products!! shouldHaveSize 2
//            response.body?.data?.totalCount shouldBe 2L
//            response.body?.data?.hasMore shouldBe false
//
//            val firstProduct = response.body?.data?.products?.first()
//            firstProduct?.name shouldBe "상품2"
//            firstProduct?.price shouldBe 20_000
//            firstProduct?.brand?.name shouldBe "브랜드2"
//            firstProduct?.summary?.likeCount shouldBe 200
//        }
//
//        it("페이징 파라미터를 사용하는 경우 - 페이지 크기를 2로 설정하면, 2개의 상품만 조회된다") {
//            // Given
//            val brand = brandJpaRepository.save(createBrand(name = "테스트 브랜드"))
//
//            // 5개의 상품 생성
//            repeat(5) { index ->
//                val product = productJpaRepository.save(
//                    createProduct(
//                        brandId = brand.id,
//                        name = "상품${index + 1}",
//                        price = (index + 1) * 1000,
//                    ),
//                )
//                productSummaryJpaRepository.save(createProductSummary(product.id, likeCount = (index + 1L) * 10))
//            }
//
//            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductListResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(
//                "$url?page=0&size=2",
//                HttpMethod.GET,
//                HttpEntity.EMPTY,
//                responseType,
//            )
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.data?.products!! shouldHaveSize 2
//            response.body?.data?.totalCount shouldBe 5L
//            response.body?.data?.hasMore shouldBe true
//        }
//
//        it("가격 오름차순으로 정렬하면, 저렴한 상품이 먼저 조회된다") {
//            // Given
//            val brand = brandJpaRepository.save(createBrand(name = "테스트 브랜드"))
//
//            val product1 = productJpaRepository.save(
//                createProduct(brandId = brand.id, name = "저렴한 상품", price = 5_000),
//            )
//            val product2 = productJpaRepository.save(
//                createProduct(brandId = brand.id, name = "비싼 상품", price = 50_000),
//            )
//
//            productSummaryJpaRepository.save(createProductSummary(product1.id, likeCount = 10))
//            productSummaryJpaRepository.save(createProductSummary(product2.id, likeCount = 100))
//
//            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductListResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(
//                "$url?sortBy=price_asc",
//                HttpMethod.GET,
//                HttpEntity.EMPTY,
//                responseType,
//            )
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.data?.products!! shouldHaveSize 2
//            response.body?.data?.products?.first()?.name shouldBe "저렴한 상품"
//            response.body?.data?.products?.first()?.price shouldBe 5000
//        }
//
//        it("좋아요 내림차순으로 정렬하면, 좋아요가 많은 상품이 먼저 조회된다") {
//            // Given
//            val brand = brandJpaRepository.save(createBrand(name = "테스트 브랜드"))
//
//            productJpaRepository.save(
//                createProduct(brandId = brand.id, name = "저렴한 상품", price = 5_000, likeCount = 10),
//            )
//            productJpaRepository.save(
//                createProduct(brandId = brand.id, name = "비싼 상품", price = 50_000, likeCount = 100),
//            )
//
//            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductListResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(
//                "$url?sortBy=likes_desc",
//                HttpMethod.GET,
//                HttpEntity.EMPTY,
//                responseType,
//            )
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.data?.products!! shouldHaveSize 2
//            response.body?.data?.products?.first()?.name shouldBe "비싼 상품"
//            response.body?.data?.products?.first()?.summary?.likeCount shouldBe 100
//        }
//
//        it("상품이 존재하지 않는 경우 - 빈 목록을 반환한다") {
//            // Given
//            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductListResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType)
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.data?.products!! shouldHaveSize 0
//            response.body?.data?.totalCount shouldBe 0L
//            response.body?.data?.hasMore shouldBe false
//        }
//
//        it("잘못된 정렬 파라미터를 사용하는 경우 - 기본 정렬(latest)로 조회된다") {
//            // Given
//            val brand = brandJpaRepository.save(createBrand(name = "테스트 브랜드"))
//            val product = productJpaRepository.save(createProduct(brandId = brand.id))
//            productSummaryJpaRepository.save(createProductSummary(product.id))
//
//            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductListResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(
//                "$url?sortBy=invalid_sort",
//                HttpMethod.GET,
//                HttpEntity.EMPTY,
//                responseType,
//            )
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.data?.products!! shouldHaveSize 1
//        }
//
//        it("브랜드 식별자로 조회하는 경우 - 해당 브랜드의 상품이 반환된다") {
//            // Given
//            val brand1 = brandJpaRepository.save(createBrand(name = "브랜드1"))
//            val brand2 = brandJpaRepository.save(createBrand(name = "브랜드2"))
//            val product1 = productJpaRepository.save(createProduct(brandId = brand1.id))
//            val product2 = productJpaRepository.save(createProduct(brandId = brand1.id))
//            val product3 = productJpaRepository.save(createProduct(brandId = brand2.id))
//            val product4 = productJpaRepository.save(createProduct(brandId = brand2.id))
//            productSummaryJpaRepository.save(createProductSummary(product1.id, likeCount = 150))
//            productSummaryJpaRepository.save(createProductSummary(product2.id, likeCount = 150))
//            productSummaryJpaRepository.save(createProductSummary(product3.id, likeCount = 150))
//            productSummaryJpaRepository.save(createProductSummary(product4.id, likeCount = 150))
//
//            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductListResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(
//                "$url?brandId=${brand2.id}",
//                HttpMethod.GET,
//                HttpEntity.EMPTY,
//                responseType,
//            )
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.data?.products!! shouldHaveSize 2
//            response.body?.data?.products?.forAll { it.brandId shouldBe brand2.id }
//        }
//    }
//
//    /**
//     * @see ProductV1Controller.get
//     */
//    describe("GET /api/v1/products/{productId}") {
//
//        it("존재하는 상품을 조회하는 경우 - 상품 상세 정보가 반환된다") {
//            // Given
//            val brand = brandJpaRepository.save(createBrand(name = "테스트 브랜드", description = "브랜드 설명"))
//            val product = productJpaRepository.save(createProduct(brandId = brand.id, name = "테스트 상품", price = 5_000))
//            productSummaryJpaRepository.save(createProductSummary(product.id, likeCount = 150))
//
//            val url = "/api/v1/products/${product.id}"
//            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductItemResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType)
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            val productData = response.body?.data!!
//
//            productData.id shouldBe product.id
//            productData.brandId shouldBe brand.id
//            productData.name shouldBe "테스트 상품"
//            productData.price shouldBe 5_000
//
//            // 브랜드 정보 검증
//            productData.brand.id shouldBe brand.id
//            productData.brand.name shouldBe "테스트 브랜드"
//            productData.brand.description shouldBe "브랜드 설명"
//
//            // 요약 정보 검증
//            productData.summary.likeCount shouldBe 150
//        }
//
//        it("존재하지 않는 상품을 조회하는 경우 - 404 Not Found가 반환된다") {
//            // Given
//            val nonExistentProductId = 999999L
//            val url = "/api/v1/products/$nonExistentProductId"
//            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductItemResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType)
//
//            // Then
//            response.statusCode shouldBe HttpStatus.NOT_FOUND
//        }
//
//        it("잘못된 상품 ID 형식을 사용하는 경우 - 400 Bad Request가 반환된다") {
//            // Given
//            val invalidProductId = "invalid"
//            val url = "/api/v1/products/$invalidProductId"
//            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductItemResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType)
//
//            // Then
//            response.statusCode shouldBe HttpStatus.BAD_REQUEST
//        }
//
//        it("상품은 존재하지만 브랜드가 삭제된 경우 - 404 Not Found가 반환된다") {
//            // Given
//            val brand = brandJpaRepository.save(createBrand(name = "삭제될 브랜드"))
//            val product = productJpaRepository.save(createProduct(brandId = brand.id, name = "고아 상품"))
//            productSummaryJpaRepository.save(createProductSummary(product.id))
//
//            // 브랜드 삭제 (실제로는 soft delete 등을 시뮬레이션)
//            brandJpaRepository.delete(brand)
//
//            val url = "/api/v1/products/${product.id}"
//            val responseType = object : ParameterizedTypeReference<ApiResponse<ProductItemResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType)
//
//            // Then
//            response.statusCode shouldBe HttpStatus.NOT_FOUND
//        }
//    }
 })
