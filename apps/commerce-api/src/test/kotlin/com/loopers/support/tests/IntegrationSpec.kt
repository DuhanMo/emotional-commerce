package com.loopers.support.tests

import com.loopers.utils.DatabaseCleanUp
import com.loopers.utils.RedisCleanUp
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.springframework.beans.factory.annotation.Autowired

@IntegrationTest
abstract class IntegrationSpec(
    body: BehaviorSpec.() -> Unit = {},
) : BehaviorSpec(body) {
    @Autowired
    private lateinit var databaseCleanUp: DatabaseCleanUp

    @Autowired
    private lateinit var redisCleanUp: RedisCleanUp

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        databaseCleanUp.truncateAllTables()
        redisCleanUp.truncateAll()
        super.afterEach(testCase, result)
    }
}
