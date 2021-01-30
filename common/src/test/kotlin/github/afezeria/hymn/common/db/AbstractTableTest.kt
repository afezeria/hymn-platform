package github.afezeria.hymn.common.db

import github.afezeria.hymn.common.adminConn
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.ktorm.schema.LongSqlType
import java.math.BigDecimal
import kotlin.reflect.full.memberProperties

/**
 * @author afezeria
 */
internal class AbstractTableTest {
    companion object {
        val table = TestTables()
        val entity = TestTable(1, 2L, 3.0, BigDecimal("4.5"), true).apply {
            orgId = "abc"
        }
        val dao = TestTableDao(TestDatabaseServiceImpl)

        @JvmStatic
        @BeforeAll
        fun before() {
            Sql.init()
        }

        @JvmStatic
        @AfterAll
        fun after() {
            Sql.clear()
        }
    }

    @Test
    fun getFieldCount() {
        table.fieldCount shouldBe 17
    }

    @Test
    fun hasHistory() {
        table.hasHistory() shouldBe true
    }

    @Test
    fun getColumnByFieldName() {
        val column = table.getColumnByFieldName("longa")
        column shouldNotBe null
        column!!.name shouldBe "longa"
        column.sqlType shouldBe LongSqlType
    }

    @Test
    fun getEntityFieldList() {
        table.getEntityFieldList().apply {
            size shouldBe 17
            val fieldNames = map { it.field.name }
            fieldNames shouldContainAll TestTable::class.memberProperties.map { it.name }
        }
    }

    @Test
    fun getEntityValueByFieldName() {
        table.getEntityValueByFieldName(entity, "inta") shouldBe 1
        table.getEntityValueByFieldName(entity, "orgId") shouldBe "abc"
        table.getEntityValueByFieldName(entity, "orgName") shouldBe null
    }

    @Test
    fun getEntityValueByColumnName() {
        table.getEntityValueByColumnName(entity, "inta") shouldBe 1
        table.getEntityValueByColumnName(entity, "org_id") shouldBe "abc"
        table.getEntityValueByColumnName(entity, "org_name") shouldBe null
    }

    @Test
    fun doCreateEntity() {
        adminConn.use {
            it.execute(
                """
                    insert into test_dao.test_table (id, inta, longa, doublea, decimala, boola, lazya, create_by_id, create_by,
                                 modify_by_id, modify_by, role_id, role_name, org_id, org_name, create_date,
                                 modify_date)
                    values ('cc25e3c045390139d47e1cb6d0c265af', 2, 42424204040, 3.2, 42.42420913, true, 'abc',
                            'cc25e3c045390139d47e1cb6d0c265af', 'admin', 'cc25e3c045390139d47e1cb6d0c265af', 'admin',
                            'cc25e3c045390139d47e1cb6d0c265af', 'admin', 'cc25e3c045390139d47e1cb6d0c265af', 'admin', now(), now()),
                           ('d2dbcc6045390139d47f1cb6d0c265af', 2, null, 3.2, 42.42420913, true, true,
                            'cc25e3c045390139d47e1cb6d0c265af', 'admin', 'cc25e3c045390139d47e1cb6d0c265af', 'admin',
                            'cc25e3c045390139d47e1cb6d0c265af', 'admin', 'cc25e3c045390139d47e1cb6d0c265af', 'admin', now(), now()),
                           ('aaabcc6045390139d47f1cb6d0c265af', null, null, 3.2, 42.42420913, true, true,
                            'cc25e3c045390139d47e1cb6d0c265af', 'admin', 'cc25e3c045390139d47e1cb6d0c265af', 'admin',
                            'cc25e3c045390139d47e1cb6d0c265af', 'admin', 'cc25e3c045390139d47e1cb6d0c265af', 'admin', now(), now()),
                           ('cfc431c0453a0139d4811cb6d0c265af', 2, null, 3.2, 42.42420913, true, null,
                            'cc25e3c045390139d47e1cb6d0c265af', 'admin', 'cc25e3c045390139d47e1cb6d0c265af', 'admin',
                            'cc25e3c045390139d47e1cb6d0c265af', 'admin', 'cc25e3c045390139d47e1cb6d0c265af', 'admin', now(), now());
            """
            )
        }
        shouldNotThrow<IllegalArgumentException> {
            dao.selectById("cc25e3c045390139d47e1cb6d0c265af")
        }
        shouldNotThrow<IllegalArgumentException> {
            dao.selectById("d2dbcc6045390139d47f1cb6d0c265af")
        }
        shouldThrow<IllegalArgumentException> {
            dao.selectById("aaabcc6045390139d47f1cb6d0c265af")
        }.apply {
            message shouldContain "github.afezeria.hymn.common.db.TestTable.inta"
        }
        shouldThrow<IllegalArgumentException> {
            dao.selectById("cfc431c0453a0139d4811cb6d0c265af")
        }.apply {
            message shouldContain "github.afezeria.hymn.common.db.TestTable.lazya"
        }
    }
}