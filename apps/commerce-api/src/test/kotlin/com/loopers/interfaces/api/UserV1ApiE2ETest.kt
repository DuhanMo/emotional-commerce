package com.loopers.interfaces.api

import com.loopers.domain.user.Gender
import com.loopers.interfaces.api.user.UserRegisterRequest
import com.loopers.interfaces.api.user.UserRegisterResponse
import com.loopers.utils.DatabaseCleanUp
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserV1ApiE2ETest(
    private val testRestTemplate: TestRestTemplate,
    private val databaseCleanUp: DatabaseCleanUp,
) : DescribeSpec({
    afterEach {
        databaseCleanUp.truncateAllTables()
    }

    /**
     * @see com.loopers.interfaces.api.user.UserV1Controller.register
     */
    describe("POST /api/v1/users") {
        val url = "/api/v1/users"

        it("회원 가입이 성공하는 경우, 생성된 유저 정보를 응답으로 반환한다") {
            val request = UserRegisterRequest("test123", "test@email.com", "2000-01-01", Gender.MALE)
            val responseType = object : ParameterizedTypeReference<ApiResponse<UserRegisterResponse>>() {}
            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request), responseType)

            response.statusCode.is2xxSuccessful shouldBe true
            response.body?.data?.id shouldBe 1L
            response.body?.data?.uid shouldBe "test123"
            response.body?.data?.email shouldBe "test@email.com"
            response.body?.data?.birthDate shouldBe "2000-01-01"
            response.body?.data?.gender shouldBe Gender.MALE
        }

        it("회원 가입시에 성별이 없는 경우, 400 Bad Request 응답을 반환한다") {
            val request = UserRegisterRequest("test123", "test@email.com", "2000-01-01", gender = null)
            val responseType = object : ParameterizedTypeReference<ApiResponse<UserRegisterResponse>>() {}
            val response = testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request), responseType)

            response.statusCode shouldBe HttpStatus.BAD_REQUEST
        }
    }
})
