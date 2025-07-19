package com.loopers.interfaces.api.user

import com.loopers.domain.user.Email
import com.loopers.domain.user.Gender
import com.loopers.domain.user.LoginId
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.interfaces.api.ApiResponse
import com.loopers.support.fixture.createUser
import com.loopers.support.tests.E2ETest
import com.loopers.utils.DatabaseCleanUp
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@E2ETest
class UserV1ApiE2ETest(
    private val userJpaRepository: UserJpaRepository,
    private val testRestTemplate: TestRestTemplate,
    private val databaseCleanUp: DatabaseCleanUp,
) : DescribeSpec({
    afterEach {
        databaseCleanUp.truncateAllTables()
    }

    /**
     * @see UserV1Controller.register
     */
    describe("POST /api/v1/users") {
        val url = "/api/v1/users"

        context("회원 가입이 성공하는 경우") {
            val request = UserRegisterRequest("test123", "test@test.com", "2000-01-01", Gender.MALE)
            val responseType = object : ParameterizedTypeReference<ApiResponse<UserResponse>>() {}

            it("생성된 유저 정보를 응답으로 반환한다") {
                val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request), responseType)

                response.statusCode.is2xxSuccessful shouldBe true
                response.body?.data?.id shouldBe 1L
                response.body?.data?.loginId shouldBe "test123"
                response.body?.data?.email shouldBe "test@test.com"
                response.body?.data?.birthDate shouldBe "2000-01-01"
                response.body?.data?.gender shouldBe Gender.MALE
            }
        }
        context("회원 가입시에 성별이 없는 경우") {
            val request = UserRegisterRequest("test123", "test@test.com", "2000-01-01", gender = null)
            val responseType = object : ParameterizedTypeReference<ApiResponse<UserResponse>>() {}

            it("400 Bad Request 응답을 반환한다") {
                val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request), responseType)

                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }
        }
    }

    /**
     * @see UserV1Controller.getMe
     */
    describe("GET /api/v1/users/me") {
        val url = "/api/v1/users/me"

        context("내 정보가 존재하는 경우") {
            userJpaRepository.save(
                createUser(
                    loginId = LoginId("test123"),
                    email = Email("test@test.com"),
                    birthDate = "2025-01-01",
                    gender = Gender.MALE,
                ),
            )
            val headers = HttpHeaders().apply { set("X-USER-ID", "test123") }
            val responseType = object : ParameterizedTypeReference<ApiResponse<UserResponse>>() {}

            it("내 정보를 조회하면, 해당하는 유저 정보를 응답으로 반환한다") {
                val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), responseType)

                response.statusCode.is2xxSuccessful shouldBe true
                response.body?.data?.loginId shouldBe "test123"
                response.body?.data?.email shouldBe "test@test.com"
                response.body?.data?.birthDate shouldBe "2025-01-01"
                response.body?.data?.gender shouldBe Gender.MALE
            }
        }
        context("내 정보가 존재하지 않는 경우") {
            val headers = HttpHeaders().apply { set("X-USER-ID", "xyz789") }
            val responseType = object : ParameterizedTypeReference<ApiResponse<UserResponse>>() {}

            it("내 정보를 조회하면, 404 Not Found 응답을 반환한다") {
                val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), responseType)

                response.statusCode shouldBe HttpStatus.NOT_FOUND
            }
        }
    }
})