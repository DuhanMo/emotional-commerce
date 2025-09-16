package com.commerce.duhan.api.point

import com.commerce.duhan.api.support.ApiResponse
import com.commerce.duhan.db.point.PointJpaRepository
import com.commerce.duhan.db.user.UserJpaRepository
import com.commerce.duhan.domain.fixtures.createPoint
import com.commerce.duhan.domain.fixtures.createUser
import com.commerce.duhan.domain.point.Point
import com.commerce.duhan.support.E2ESpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class PointV1ControllerE2ETest(
    private val userJpaRepository: UserJpaRepository,
    private val pointJpaRepository: PointJpaRepository,
    private val testRestTemplate: TestRestTemplate,
) : E2ESpec({
    /**
     * @see PointV1Controller.charge
     */
    describe("POST /api/v1/points/charge") {
        val url = "/api/v1/points/charge"

        context("유저가 1000원을 충전하는 경우") {
            it("충전 후 보유 총량을 반환한다") {
                // given
                val user = userJpaRepository.save(createUser())
                pointJpaRepository.save(createPoint(user.id, 2000))

                val headers = HttpHeaders().apply { set("X-USER-ID", "${user.id}") }
                val request = ChargePointRequest(1000)
                val responseType = object : ParameterizedTypeReference<ApiResponse<PointResponse>>() {}

                // when
                val response = testRestTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    HttpEntity(request, headers),
                    responseType,
                )

                // then
                response.statusCode.is2xxSuccessful shouldBe true
                response.body?.data?.amount shouldBe 3000
            }
        }

        context("유저가 존재하지 않는 경우") {
            it("404 Not Found 응답을 반환한다") {
                // given
                val user = userJpaRepository.save(createUser())
                pointJpaRepository.save(Point(user.id, 2000))

                val headers = HttpHeaders().apply { set("X-USER-ID", "999") }
                val request = ChargePointRequest(1000)
                val responseType = object : ParameterizedTypeReference<ApiResponse<PointResponse>>() {}

                // when
                val response = testRestTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    HttpEntity(request, headers),
                    responseType,
                )

                // then
                response.statusCode shouldBe HttpStatus.NOT_FOUND
            }
        }
    }

    /**
     * @see PointV1Controller.find
     */
    describe("GET /api/v1/points") {
        val url = "/api/v1/points"

        context("회원이 존재할 경우") {
            it("보유 포인트가 반환된다") {
                // given
                val user = userJpaRepository.save(createUser())
                pointJpaRepository.save(Point(user.id, 500))
                val headers = HttpHeaders().apply { set("X-USER-ID", "${user.id}") }
                val responseType = object : ParameterizedTypeReference<ApiResponse<PointResponse>>() {}

                // when
                val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), responseType)

                // then
                response.statusCode.is2xxSuccessful shouldBe true
                response.body?.data?.amount shouldBe 500
            }
        }

        context("X-USER-ID 헤더가 없을 경우") {
            it("400 Bad Request 응답을 반환한다") {
                // given
                val headers = HttpHeaders()
                val responseType = object : ParameterizedTypeReference<ApiResponse<PointResponse>>() {}

                // when
                val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), responseType)

                // then
                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }
        }
    }
})
