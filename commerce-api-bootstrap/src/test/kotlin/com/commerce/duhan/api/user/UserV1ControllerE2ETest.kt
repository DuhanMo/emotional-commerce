package com.commerce.duhan.api.user

import com.commerce.duhan.api.support.ApiResponse
import com.commerce.duhan.core.domain.user.Gender
import com.commerce.duhan.core.domain.user.LoginId
import com.commerce.duhan.core.fixtures.createUser
import com.commerce.duhan.db.user.UserJpaRepository
import com.commerce.duhan.support.E2ESpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class UserV1ControllerE2ETest(
    private val testRestTemplate: TestRestTemplate,
    private val userJpaRepository: UserJpaRepository,
) : E2ESpec({
    /**
     * @see UserV1Controller.create
     */
    describe("POST /api/v1/users") {
        val url = "/api/v1/users"

        context("회원 가입이 성공하는 경우") {
            it("생성된 유저 정보를 응답으로 반환한다") {
                // given
                val request = CreateUserRequest("test123", "test@test.com", "2000-01-01", Gender.MALE)
                val responseType = object : ParameterizedTypeReference<ApiResponse<UserResponse>>() {}

                // when
                val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request), responseType)

                // then
                response.statusCode.is2xxSuccessful shouldBe true
                response.body?.data?.id shouldBe 1L
                response.body?.data?.loginId shouldBe "test123"
                response.body?.data?.email shouldBe "test@test.com"
                response.body?.data?.birthDate shouldBe "2000-01-01"
                response.body?.data?.gender shouldBe Gender.MALE
            }
        }

        context("로그인 아이디가 이미 존재하는 경우") {
            it("회원가입에 실패한다") {
                // given
                val existLoginId = "exist123"
                userJpaRepository.save(createUser(loginId = LoginId(existLoginId)))
                val request = CreateUserRequest(existLoginId, "test@test.com", "2000-01-01", Gender.FEMALE)
                val responseType = object : ParameterizedTypeReference<ApiResponse<UserResponse>>() {}

                // when
                val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request), responseType)

                // then
                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }
        }
    }
    /**
     * @see UserV1Controller.getMe
     */
    describe("GET /api/v1/users/me") {
        val url = "/api/v1/users/me"

        context("회원가입 후") {
            context("내 정보를 조회하는 경우") {
                it("해당하는 유저 정보를 응답으로 반환한다") {
                    // given
                    val createUserRequest = CreateUserRequest("test123", "test@test.com", "2000-01-01", Gender.MALE)
                    val userResponseType = object : ParameterizedTypeReference<ApiResponse<UserResponse>>() {}

                    // 회원가입 요청
                    val createUserResponse =
                        testRestTemplate.exchange("/api/v1/users", HttpMethod.POST, HttpEntity(createUserRequest), userResponseType)

                    val headers = HttpHeaders().apply { set("X-USER-ID", "${createUserResponse?.body?.data?.id}") }
                    val responseType = object : ParameterizedTypeReference<ApiResponse<GetMeResponse>>() {}

                    // when
                    // 내 정보 조회
                    val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), responseType)

                    // then
                    response.statusCode.is2xxSuccessful shouldBe true
                    response.body?.data?.user?.loginId shouldBe "test123"
                    response.body?.data?.user?.email shouldBe "test@test.com"
                    response.body?.data?.user?.birthDate shouldBe "2000-01-01"
                    response.body?.data?.user?.gender shouldBe Gender.MALE
                    response.body?.data?.point?.amount shouldBe 0
                }
            }
        }

        context("내 정보가 존재하지 않는 경우") {
            it("404 Not Found 응답을 반환한다") {
                // given
                val headers = HttpHeaders().apply { set("X-USER-ID", "9999") }
                val responseType = object : ParameterizedTypeReference<ApiResponse<UserResponse>>() {}

                // when
                val response = testRestTemplate.exchange(url, HttpMethod.GET, HttpEntity(null, headers), responseType)

                // then
                response.statusCode shouldBe HttpStatus.NOT_FOUND
            }
        }
    }
})
