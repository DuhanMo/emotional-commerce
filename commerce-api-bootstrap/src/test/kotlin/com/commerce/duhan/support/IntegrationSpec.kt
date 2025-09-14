package com.commerce.duhan.support

import com.commerce.duhan.db.utils.DatabaseCleanUp
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
abstract class IntegrationSpec(
    body: DescribeSpec.() -> Unit = {},
) : DescribeSpec(body) {
    @Autowired
    private lateinit var databaseCleanUp: DatabaseCleanUp

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        databaseCleanUp.truncateAllTables()
        super.afterEach(testCase, result)
    }
}
