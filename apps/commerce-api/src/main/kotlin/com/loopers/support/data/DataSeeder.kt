package com.loopers.support.data

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.Statement
import javax.sql.DataSource
import kotlin.math.min
import net.datafaker.Faker
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("local")
class DataSeeder(
    private val dataSource: DataSource,
    @Value("\${app.seed.enable:true}") private val enable: Boolean,
    @Value("\${app.seed.brands:100}") private val brandCount: Int,
    @Value("\${app.seed.products-per-brand:50}") private val productsPerBrand: Int,
    @Value("\${app.seed.batch-size:2000}") private val batchSize: Int,
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val faker = Faker()

    override fun run(vararg args: String?) {
        if (!enable) return

        dataSource.connection.use { conn ->
            conn.autoCommit = false
            try {
                val hasBrand = hasAny(conn, "brand")
                val hasProduct = hasAny(conn, "product")
                val hasSummary = hasAny(conn, "product_summary")

                if (hasBrand || hasProduct || hasSummary) {
                    logger.info(
                        "Seed skipped: existing data detected " +
                                "(brand=$hasBrand, product=$hasProduct, product_summary=$hasSummary)",
                    )
                    conn.rollback()
                    return
                }

                val brandIds = insertBrands(conn, brandCount, batchSize)
                val productIds = insertProducts(conn, brandIds, productsPerBrand, batchSize)
                insertProductSummaries(conn, productIds, batchSize)

                conn.commit()
                logger.info(
                    "Seed completed: brands=${brandIds.size}, products=${productIds.size}, product_summaries=${productIds.size}",
                )
            } catch (e: Exception) {
                conn.rollback()
                throw e
            } finally {
                conn.autoCommit = true
            }
        }
    }

    private fun hasAny(conn: Connection, table: String): Boolean =
        conn.prepareStatement("SELECT 1 FROM $table WHERE deleted_at IS NULL LIMIT 1").use { ps ->
            ps.executeQuery().use { rs -> rs.next() }
        }

    private fun insertBrands(conn: Connection, count: Int, batch: Int): List<Long> {
        val sql = """
            INSERT INTO brand (name, description, logo_url, created_at, updated_at, deleted_at)
            VALUES (?, ?, ?, NOW(), NOW(), NULL)
        """.trimIndent()
        val ids = mutableListOf<Long>()

        conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { ps ->
            var i = 0
            while (i < count) {
                val limit = min(batch, count - i)
                repeat(limit) {
                    val name = uniqueBrandName(i + it + 1)
                    ps.setString(1, name)
                    ps.setString(2, faker.lorem().sentence(6))
                    ps.setString(3, "https://picsum.photos/seed/brand_${i + it + 1}/200/200")
                    ps.addBatch()
                }
                ps.executeBatch()
                collectKeys(ps, ids)
                i += limit
            }
        }
        return ids
    }

    private fun uniqueBrandName(seq: Int): String {
        val base = faker.company().name().replace("'", "")
        return "$base #$seq"
    }

    private fun insertProducts(
        conn: Connection,
        brandIds: List<Long>,
        perBrand: Int,
        batch: Int,
    ): List<Long> {
        val sql = """
            INSERT INTO product (brand_id, name, description, price, stock, image_url, created_at, updated_at, deleted_at)
            VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW(), NULL)
        """.trimIndent()
        val ids = mutableListOf<Long>()

        conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { ps ->
            var pending = 0
            for (bId in brandIds) {
                repeat(perBrand) {
                    ps.setLong(1, bId)
                    ps.setString(2, faker.commerce().productName().take(100))
                    ps.setString(3, faker.lorem().sentence(10))
                    ps.setInt(4, faker.number().numberBetween(5_000, 500_000))
                    ps.setInt(5, faker.number().numberBetween(0, 1_000))
                    ps.setString(6, "https://picsum.photos/seed/product_${bId}_$it/400/400")
                    ps.addBatch()
                    pending++

                    if (pending >= batch) {
                        ps.executeBatch()
                        collectKeys(ps, ids)
                        pending = 0
                    }
                }
            }
            if (pending > 0) {
                ps.executeBatch()
                collectKeys(ps, ids)
            }
        }
        return ids
    }

    private fun insertProductSummaries(conn: Connection, productIds: List<Long>, batch: Int) {
        val sql = """
            INSERT INTO product_summary (product_id, like_count, version, created_at, updated_at, deleted_at)
            VALUES (?, 0, 0, NOW(), NOW(), NULL)
        """.trimIndent()

        conn.prepareStatement(sql).use { ps ->
            var pending = 0
            for (pId in productIds) {
                ps.setLong(1, pId)
                ps.addBatch()
                pending++
                if (pending >= batch) {
                    ps.executeBatch()
                    pending = 0
                }
            }
            if (pending > 0) ps.executeBatch()
        }
    }

    private fun collectKeys(ps: PreparedStatement, out: MutableList<Long>) {
        ps.generatedKeys.use { rs ->
            while (rs.next()) out += rs.getLong(1)
        }
    }
}
