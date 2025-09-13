package com.commerce.duhan.api.user

import com.commerce.duhan.api.support.ApiResponse
import com.commerce.duhan.api.support.E2ESpec
import com.commerce.duhan.core.domain.user.Gender
import com.commerce.duhan.core.domain.user.Gender.FEMALE
import com.commerce.duhan.core.domain.user.LoginId
import com.commerce.duhan.core.fixtures.createUser
import com.commerce.duhan.db.user.UserJpaRepository
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
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
                val responseType = object : ParameterizedTypeReference<ApiResponse<CreateUserResponse>>() {}

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
                val request = CreateUserRequest(existLoginId, "test@test.com", "2000-01-01", FEMALE)
                val responseType = object : ParameterizedTypeReference<ApiResponse<CreateUserResponse>>() {}

                // when
                val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request), responseType)

                // then
                response.statusCode shouldBe HttpStatus.BAD_REQUEST
            }
        }
    }
})
