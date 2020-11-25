package github.afezeria.hymn.common.sql.trigger

import github.afezeria.hymn.common.conn
import github.afezeria.hymn.common.sql.DEFAULT_ACCOUNT_ID
import github.afezeria.hymn.common.sql.DEFAULT_ACCOUNT_NAME
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.*
import org.postgresql.util.PSQLException
import java.time.LocalDateTime

/**
 * @author afezeria
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ModuleObjectTest {
    companion object {
        lateinit var id: String

        @AfterAll
        @JvmStatic
        fun clear() {
            conn.use {
                it.execute("alter table hymn.sys_core_b_object disable trigger all")
                it.execute("alter table hymn.sys_core_b_object_field disable trigger all")
                it.execute(
                    "delete from hymn.sys_core_b_object where api not in (?,?,?,?)",
                    "account", "role", "org", "business_type",
                )
                it.execute("alter table hymn.sys_core_b_object enable trigger all")
                it.execute("alter table hymn.sys_core_b_object_field enable trigger all")
            }
        }

    }

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
            id = insert["id"] as String

            it.execute("select * from hymn.sys_core_b_object_field where object_id = ?", id).apply {
                size shouldBe 0
            }
        }
    }


}