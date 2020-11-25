package github.afezeria.hymn.common.sql.trigger

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
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class CustomObjectTest : BaseDbTest() {
    companion object {
        lateinit var id: String

        @AfterAll
        @JvmStatic
        fun clear() {
            //                clear
            conn.use {
//                it.execute("alter table hymn.sys_core_b_object disable trigger all")
//                it.execute("alter table hymn.sys_core_b_object_field disable trigger all")
//                it.execute(
//                    "delete from hymn.sys_core_b_object where api not in (?,?,?,?)",
//                    "account", "role", "org", "business_type",
//                )
//                it.execute("alter table hymn.sys_core_b_object enable trigger all")
//                it.execute("alter table hymn.sys_core_b_object_field enable trigger all")
            }
        }
    }

    @Test
    @Order(1)
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
            id = insert["id"] as String

//            存在默认字段
            it.execute("select * from hymn.sys_core_b_object_field where object_id = ?", id).apply {
                size shouldBe 8
                map { it["api"] } shouldContainAll listOf("name", "create_by_id", "modify_by_id", "create_date", "modify_date", "owner_id", "lock_state", "type_id")
                filter { it["api"] == "name" }.first().apply {
                    get("name") shouldBe "编号"
                    get("gen_rule") shouldBe "{yyyy}{mm}{dd}{000}"
                }
            }

            insert["source_table"] as String shouldMatch "^sys_core_data_table_\\d{3}$"

//            递增序列重置
            val seqName = "hymn.${insert["source_table"]}_seq"
            it.execute("select nextval(?)", seqName).apply {
                size shouldBe 1
                this[0]["nextval"] shouldBe 1
            }

//            创建视图
            it.execute("""
select *
from pg_class pc
         left join pg_namespace pn on pn.oid = pc.relnamespace
where pn.nspname = 'hymn_view'
  and pc.relname = ?;
""",
                insert["api"]).apply {
                size shouldBe 1
            }

            it.execute("""
select *
from pg_trigger
         left join pg_class pc on pg_trigger.tgrelid = pc.oid
         left join pg_namespace pn on pn.oid = pc.relnamespace
where pc.relname = ?
  and pn.nspname = 'hymn';
""",
                insert["source_table"]).apply {
                size shouldBe 2
//                get(0)["tgname"] shouldBe "${insert["source_table"]}_auto_numbering"
                map { it["tgname"] } shouldContainAll listOf("${insert["source_table"]}_history", "${insert["source_table"]}_auto_numbering")
            }

            val type = it.execute("select * from hymn.sys_core_b_object_type where object_id = ?", id).run {
                size shouldBe 1
                get(0)
            }
            //            插入测试数据
            it.execute(
                """
insert into hymn_view.${insert["api"]} (lock_state, type_id, owner_id, modify_by_id, create_by_id, modify_date, create_date)
values (0,?,?,?,?,now(),now());
            """,
                type["id"], DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID
            )
            it.execute("select * from hymn_view.${insert["api"]}").size shouldBe 1

        }
    }

    @Test
    @Order(2)
    fun `delete active object`() {
        conn.use {
            val e = shouldThrow<PSQLException> {
                it.execute("delete from hymn.sys_core_b_object where id = ?", id)
            }
            e.sqlState shouldBe "P0001"
            e.message shouldContain "cannot delete active object"
        }
    }

    @Test
    @Order(3)
    fun `update source_table`() {
        conn.use {
            val e = shouldThrow<PSQLException> {
                it.execute("update hymn.sys_core_b_object set source_table = 'sys_core_account' where id = ?", id)
            }
            e.sqlState shouldBe "P0001"
            e.message shouldContain "cannot modify source_table"
        }
    }

    @Test
    @Order(4)
    fun `update api`() {
        conn.use {
            val e = shouldThrow<PSQLException> {
                it.execute("update hymn.sys_core_b_object set api = 'test_object_2' where id = ?", id)
            }
            e.sqlState shouldBe "P0001"
            e.message shouldContain "cannot change api"
        }
    }

    @Test
    @Order(6)
    fun `deactivate object`() {
        conn.use {
            it.execute("update hymn.sys_core_b_object set active = false where id = ?", id)
            val field = it.execute("select * from hymn.sys_core_b_object_field where object_id = ? and api = 'name'", id)[0]
//            更新关联数据
            var e = shouldThrow<PSQLException> {
                it.execute("update hymn.sys_core_b_object_field set name = '测试对象编号' where id =?", field["id"])
            }
            e.sqlState shouldBe "P0001"
            e.message shouldContain "object is inactive, cannot insert/update/delete related data"

//            删除关联数据
            e = shouldThrow {
                it.execute("delete from hymn.sys_core_b_object_field where  id= ?", field["id"])
            }
            e.sqlState shouldBe "P0001"
            e.message shouldContain "object is inactive, cannot insert/update/delete related data"
//            新增关联数据
            e = shouldThrow {
                it.execute("insert into hymn.sys_core_b_object_field  (object_id, name, api, type, default_value, formula, max_length, min_length, visible_row, dict_id, master_field_id, optional_number, ref_id, ref_list_label, ref_allow_delete, query_filter, s_id, s_field_id, s_type, gen_rule, remark, help, tmp, standard_type, is_standard, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) values (?,'文本','atext','text',null,null,255,1,1,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,false,?,?,?,?,now(),now());", id, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME)
            }
            e.sqlState shouldBe "P0001"
            e.message shouldContain "object is inactive, cannot insert/update/delete related data"
        }
    }

    @Test
    @Order(7)
    fun `delete object`() {
        conn.use {
            val del = it.execute("select * from hymn.sys_core_b_object where id=?", id)[0]

            it.execute("delete from hymn.sys_core_b_object where id=?", id)
            it.execute("select * from hymn.sys_core_b_object_field where object_id=?", id).size shouldBe 0
            it.execute("select * from hymn.sys_core_b_object_type where object_id=?", id).size shouldBe 0
            it.execute("select * from hymn.${del["source_table"]}").size shouldBe 0
            it.execute("select * from hymn.${del["source_table"]}_history").size shouldBe 0
            it.execute("""
                select * from pg_class pc
                left join pg_namespace pn on pc.relnamespace=pn.oid
                where pn.nspname='hymn_view'
                and pc.relname = ?
            """, del["api"]).size shouldBe 0
            it.execute("""
                select pt.*
                from pg_trigger pt
                         left join pg_class pc on pt.tgrelid = pc.oid
                         left join pg_namespace pn on pn.oid = pc.relnamespace
                where pc.relname = ?
                  and pn.nspname = 'hymn';

            """, del["source_table"]).size shouldBe 0
        }
    }


}