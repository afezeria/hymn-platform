package github.afezeria.hymn.common.sql.obj

import github.afezeria.hymn.common.conn
import github.afezeria.hymn.common.sql.BaseDbTest
import github.afezeria.hymn.common.sql.DEFAULT_ACCOUNT_ID
import github.afezeria.hymn.common.sql.DEFAULT_ACCOUNT_NAME
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldMatch
import org.junit.jupiter.api.*
import org.postgresql.util.PSQLException
import java.time.LocalDateTime

/**
 * @author afezeria
 */
class BusinessObjectInsertTriggerTest : BaseDbTest() {
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
    inner class CustomObject {
        @Test
        fun insert() {
            conn.use {
                val insert = it.execute("""
insert into hymn.sys_core_b_object(name,api,active,remark,create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
values ('测试对象','test_obj',true,'编号
{yyyy}{mm}{dd}{000}',?,?,?,?,?,?) returning *;""",
                    DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME,
                    LocalDateTime.now(), LocalDateTime.now()
                )[0]
                insert["id"] shouldNotBe null
                val id = insert["id"] as String

                it.execute("select * from hymn.sys_core_b_object_field where object_id = ?", id).apply {
                    size shouldBe 8
                    map { it["api"] } shouldContainAll listOf("name", "create_by_id", "modify_by_id", "create_date", "modify_date", "owner", "lock_state", "type")
                    filter { it["api"] == "name" }.first().apply {
                        get("name") shouldBe "编号"
                        get("gen_rule") shouldBe "{yyyy}{mm}{dd}{000}"
                    }
                }

                insert["source_table"] as String shouldMatch "^sys_core_data_table_\\d{3}$"
                val seqName = "hymn.${insert["source_table"]}_seq"
                it.execute("select nextval(?)", seqName).apply {
                    size shouldBe 1
                    this[0]["nextval"] shouldBe 1
                }
            }
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation::class)
    inner class ModuleObject {
        @Test
        @Order(1)
        fun `throw when not specify source_table`() {
            conn.use {
                val e = shouldThrow<PSQLException> {
                    it.execute("""
insert into hymn.sys_core_b_object(name,api,active,module_api,remark,create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
values ('模块对象','module_obj',true,'core','模块对象',?,?,?,?,?,?) returning *;""",
                        DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME,
                        LocalDateTime.now(), LocalDateTime.now()
                    )
                }
                e.sqlState shouldBe "P0001"
                e.message shouldContain "create a module object must specify source_table"
            }
        }

        @Test
        @Order(2)
        fun `throw when source_table does not exists`() {
            conn.use {
                val e = shouldThrow<PSQLException> {
                    it.execute("""
insert into hymn.sys_core_b_object(source_table,name,api,active,module_api,remark,create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
values ('sys_test_obj','模块对象','module_obj',true,'core','模块对象',?,?,?,?,?,?) returning *;""",
                        DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME,
                        LocalDateTime.now(), LocalDateTime.now()
                    )
                }
                e.sqlState shouldBe "P0001"
                e.message shouldContain "table hymn.sys_test_obj does not exists"
            }
        }

        @Test
        @Order(3)
        fun insert() {
            conn.use {
                it.execute("""
                    create table hymn.sys_test_obj(
                    id text primary key ,
                    aint int
                    )
                """)
                val insert = it.execute("""
insert into hymn.sys_core_b_object(source_table,name,api,active,module_api,remark,create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
values ('sys_test_obj','模块对象','module_obj',true,'core','模块对象',?,?,?,?,?,?) returning *;""",
                    DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME,
                    LocalDateTime.now(), LocalDateTime.now()
                )[0]
                insert["id"] shouldNotBe null
                val id = insert["id"] as String

                it.execute("select * from hymn.sys_core_b_object_field where object_id = ?", id).apply {
                    size shouldBe 0
                }
            }
        }

    }

}