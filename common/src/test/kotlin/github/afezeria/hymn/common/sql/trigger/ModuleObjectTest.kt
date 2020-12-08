package github.afezeria.hymn.common.sql.trigger

import github.afezeria.hymn.common.adminConn
import github.afezeria.hymn.common.sql.BaseDbTest
import github.afezeria.hymn.common.sql.COMMON_INFO
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.*
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ModuleObjectTest : BaseDbTest() {
    companion object {
        lateinit var id: String

        @AfterAll
        @JvmStatic
        fun clear() {
            adminConn.use {
                it.execute(
                    "update hymn.core_b_object set active=false where active=true and api not in (?,?,?,?)",
                    "account", "role", "org", "object_type",
                )
                it.execute(
                    "delete from hymn.core_b_object where api not in (?,?,?,?)",
                    "account", "role", "org", "object_type",
                )
            }
        }

    }

    @Test
    @Order(1)
    fun `throw when not specify source_table`() {
        adminConn.use {
            val e = shouldThrow<PSQLException> {
                it.execute(
                    """
insert into hymn.core_b_object(name,api,type,module_api,remark,create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
values ('模块对象','module_obj','module','core','模块对象',?,?,?,?,now(),now()) returning *;""",
                    *COMMON_INFO
                )
            }
            e.sqlState shouldBe "P0001"
            e.message shouldContain "创建模块对象必须指定数据表"
        }
    }

    @Test
    @Order(2)
    fun `throw when source_table does not exists`() {
        adminConn.use {
            val e = shouldThrow<PSQLException> {
                it.execute(
                    """
insert into hymn.core_b_object(source_table,name,api,type,module_api,remark,create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
values ('sys_test_obj','模块对象','module_obj','module','core','模块对象',?,?,?,?,now(),now()) returning *;""",
                    *COMMON_INFO
                )
            }
            e.sqlState shouldBe "P0001"
            e.message shouldContain "表 .*? 不存在".toRegex()
        }
    }

    @Test
    @Order(3)
    fun insert() {
        adminConn.use {
            it.execute(
                """
                    create table hymn.sys_test_obj(
                    id text primary key ,
                    aint int
                    )
                """
            )
            try {
                val insert = it.execute(
                    """
insert into hymn.core_b_object(source_table,name,api,type,module_api,remark,create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
values ('sys_test_obj','模块对象','module_obj','module','core','模块对象',?,?,?,?,now(),now()) returning *;""",
                    *COMMON_INFO
                )[0]
                insert["id"] shouldNotBe null
                id = insert["id"] as String

                it.execute("select * from hymn.core_b_object_field where object_id = ?", id).apply {
                    size shouldBe 0
                }
            } finally {
                it.execute("drop table if exists hymn.sys_test_obj cascade")
            }
        }
    }


}