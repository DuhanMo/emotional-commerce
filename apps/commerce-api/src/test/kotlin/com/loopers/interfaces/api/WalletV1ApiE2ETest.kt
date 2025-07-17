package com.loopers.interfaces.api

import com.loopers.domain.user.LoginId
import com.loopers.domain.user.Wallet
import com.loopers.infrastructure.user.UserJpaRepository
import com.loopers.infrastructure.user.WalletJpaRepository
import com.loopers.interfaces.api.wallet.PointChargeRequest
import com.loopers.interfaces.api.wallet.PointChargeResponse
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
class WalletV1ApiE2ETest(
    private val userJpaRepository: UserJpaRepository,
    private val walletJpaRepository: WalletJpaRepository,
    private val testRestTemplate: TestRestTemplate,
    private val databaseCleanUp: DatabaseCleanUp,
) : DescribeSpec({
    afterEach {
        databaseCleanUp.truncateAllTables()
    }
    /**
     * @see com.loopers.interfaces.api.wallet.WalletV1Controller.charge
     */
    describe("POST /api/v1/wallets/charge") {
        val url = "/api/v1/wallets/charge"

        context("존재하는 유저가 1000원을 충전하는 경우") {
            val user = userJpaRepository.save(createUser(loginId = LoginId("abc123")))
            walletJpaRepository.save(Wallet(user, 2000))
            val headers = HttpHeaders().apply { set("X-USER-ID", "abc123") }
            val request = PointChargeRequest(1000)
            val responseType = object : ParameterizedTypeReference<ApiResponse<PointChargeResponse>>() {}

            it("충전된 보유 총량을 응답으로 반환한다") {
                val response =
                    testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request, headers), responseType)

                response.statusCode.is2xxSuccessful shouldBe true
                response.body?.data?.point shouldBe 3000
            }
        }

        context("존재하지 않는 유저로 요청하는 경우") {
            val user = userJpaRepository.save(createUser(loginId = LoginId("abc123")))
            walletJpaRepository.save(Wallet(user, 2000))
            val headers = HttpHeaders().apply { set("X-USER-ID", "xyz789") }
            val request = PointChargeRequest(1000)
            val responseType = object : ParameterizedTypeReference<ApiResponse<PointChargeResponse>>() {}

            it("404 Not Found 응답을 반환한다") {
                val response =
                    testRestTemplate.exchange(url, HttpMethod.POST, HttpEntity(request, headers), responseType)

                response.statusCode shouldBe HttpStatus.NOT_FOUND
            }
        }
    }
})
