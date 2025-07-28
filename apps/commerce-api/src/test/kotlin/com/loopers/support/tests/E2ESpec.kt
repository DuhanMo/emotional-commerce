package com.loopers.support.tests

import com.loopers.utils.DatabaseCleanUp
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.springframework.beans.factory.annotation.Autowired

@E2ETest
abstract class E2ESpec(
    body: DescribeSpec.() -> Unit = {}
) : DescribeSpec(body) {
    @Autowired
    private lateinit var databaseCleanUp: DatabaseCleanUp

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        databaseCleanUp.truncateAllTables()
        super.afterEach(testCase, result)
    }
}