 package com.loopers.interfaces.api.point

 import com.loopers.infrastructure.point.PointJpaRepository
 import com.loopers.infrastructure.user.UserJpaRepository
 import com.loopers.support.tests.E2ESpec
 import org.springframework.boot.test.web.client.TestRestTemplate

 class PointV1ApiE2ETest(
    private val userJpaRepository: UserJpaRepository,
    private val pointJpaRepository: PointJpaRepository,
    private val testRestTemplate: TestRestTemplate,
 ) : E2ESpec({
//    /**
//     * @see PointV1Controller.charge
//     */
//    describe("POST /api/v1/points/charge") {
//        val url = "/api/v1/points/charge"
//
//        it("존재하는 유저가 1000원을 충전하는 경우 - 충전된 보유 총량을 응답으로 반환한다") {
//            // Given
//            val user = userJpaRepository.save(createUser(loginId = LoginId("abc123")))
//            pointJpaRepository.save(Point(user.id, 2_000))
//            val headers = HttpHeaders().apply { set("X-USER-ID", "abc123") }
//            val request = PointChargeRequest(1_000)
//            val responseType = object : ParameterizedTypeReference<ApiResponse<PointResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request, headers), responseType)
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.data?.point shouldBe 3_000
//        }
//
//        it("존재하지 않는 유저로 요청하는 경우 - 404 Not Found 응답을 반환한다") {
//            // Given
//            val user = userJpaRepository.save(createUser(loginId = LoginId("abc123")))
//            pointJpaRepository.save(Point(user.id, 2000))
//            val headers = HttpHeaders().apply { set("X-USER-ID", "xyz789") }
//            val request = PointChargeRequest(1000)
//            val responseType = object : ParameterizedTypeReference<ApiResponse<PointResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request, headers), responseType)
//
//            // Then
//            response.statusCode shouldBe HttpStatus.NOT_FOUND
//        }
//    }
//
//    describe("GET /api/v1/points") {
//        val url = "/api/v1/points"
//
//        it("해당 ID 의 회원이 존재할 경우 - 보유 포인트가 반환된다") {
//            // Given
//            val user = userJpaRepository.save(createUser(loginId = LoginId("abc123")))
//            pointJpaRepository.save(Point(user.id, 500))
//            val headers = HttpHeaders().apply { set("X-USER-ID", "abc123") }
//            val responseType = object : ParameterizedTypeReference<ApiResponse<PointResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode.is2xxSuccessful shouldBe true
//            response.body?.data?.point shouldBe 500
//        }
//
//        it("X-USER-ID 헤더가 없을 경우 - 400 Bad Request 응답을 반환한다") {
//            // Given
//            val user = userJpaRepository.save(createUser(loginId = LoginId("abc123")))
//            pointJpaRepository.save(Point(user.id, 2000))
//            val headers = HttpHeaders()
//            val responseType = object : ParameterizedTypeReference<ApiResponse<PointResponse>>() {}
//
//            // When
//            val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), responseType)
//
//            // Then
//            response.statusCode shouldBe HttpStatus.BAD_REQUEST
//        }
//    }
 })
