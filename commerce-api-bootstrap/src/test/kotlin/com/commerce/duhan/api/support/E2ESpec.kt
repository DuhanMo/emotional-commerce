package com.commerce.duhan.api.support

import com.commerce.duhan.db.utils.DatabaseCleanUp
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class E2ESpec(
    body: DescribeSpec.() -> Unit = {},
) : DescribeSpec(body) {
    @Autowired
    private lateinit var databaseCleanUp: DatabaseCleanUp

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        databaseCleanUp.truncateAllTables()
        super.afterEach(testCase, result)
    }
}
