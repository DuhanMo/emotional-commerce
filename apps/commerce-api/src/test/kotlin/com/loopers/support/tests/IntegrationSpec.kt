package com.loopers.support.tests

import com.loopers.utils.DatabaseCleanUp
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

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        databaseCleanUp.truncateAllTables()
        super.afterEach(testCase, result)
    }
}
