package github.afezeria.hymn.common.sql.bobject

import github.afezeria.hymn.common.adminConn
import github.afezeria.hymn.common.sql.BaseDbTest
import github.afezeria.hymn.common.sql.COMMON_INFO
import github.afezeria.hymn.common.sql.clearBObject
import github.afezeria.hymn.common.sql.createBObject
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class UpdateTriggerTest : BaseDbTest() {

    @AfterEach
    fun clear() {
        clearBObject()
    }

    @Test
    fun `cannot modify inactive object`() {
        val data = createBObject()
        val id = data["id"] as String
        adminConn.use {
            it.execute("update hymn.core_b_object set active=false where id =?", id)
            val up = it.execute(
                "update hymn.core_b_object set name='bbbcd' where id =? returning *",
                id
            )[0]
            up["name"] shouldNotBe "bbbcd"
        }
    }

    @Test
    fun `cannot modify other properties when a object is enabled`() {
        val data = createBObject()
        val id = data["id"] as String
        val name = data["name"] as String
        adminConn.use {
            var c = it.execute(
                "update hymn.core_b_object set active=false ,name = 'bbcd' where id =? returning *",
                id
            )[0]
            c["name"] shouldBe name
            c["active"] shouldBe false
            c = it.execute(
                "update hymn.core_b_object set name='bbbcd',active=true where id =? returning *",
                id
            )[0]
            c["name"] shouldBe name
            c["active"] shouldBe true
        }
    }

    @Test
    fun `cannot modify source table`() {
        val data = createBObject()
        val id = data["id"] as String
        adminConn.use {
            val e = shouldThrow<PSQLException> {
                it.execute(
                    "update hymn.core_b_object set source_table = 'core_account' where id = ?",
                    id
                )
            }
            e.sqlState shouldBe "P0001"
            e.message shouldContain "不能修改source_table"
        }
    }

    @Test
    fun `object cannot be inactive when it is referenced`() {
        val o1 = createBObject()
        val o1Id = o1["id"] as String
        val o2 = createBObject()
        val o2Id = o2["id"] as String
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_delete_policy,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'关联','reffield','reference',?,'null',?,?,?,?,now(),now()) returning *;
                    """,
                o1Id, o2Id, *COMMON_INFO
            )[0]
            val e = shouldThrow<PSQLException> {
                it.execute("update hymn.core_b_object set active=false where id = ?", o2Id)
            }
            e.message shouldContain "不能停用当前对象，以下字段引用了当前对象"
            it.execute(
                "update hymn.core_b_object_field  set active = false where id=?;",
                field["id"]
            )
            shouldNotThrow<PSQLException> {
                it.execute("update hymn.core_b_object set active=false where id = ?", o2Id)
            }
        }
    }

    @Test
    fun `create or drop view when active property change`() {
        val o = createBObject()
        val id = o["id"] as String
        val api = o["api"] as String
        adminConn.use {
            it.execute(
                """
                    select * from pg_class pc left join pg_namespace pn on pc.relnamespace = pn.oid
                    where pn.nspname='hymn_view' and pc.relname = ?
                """, api
            ).size shouldBe 1
//            停用对象删除视图
            it.execute("update hymn.core_b_object set active=false where id =?", id)
            it.execute(
                """
                    select * from pg_class pc left join pg_namespace pn on pc.relnamespace = pn.oid
                    where pn.nspname='hymn_view' and pc.relname = ?
                """, api
            ).size shouldBe 0

//            启用对象创建视图
            it.execute("update hymn.core_b_object set active=true where id =?", id)
            it.execute(
                """
                    select * from pg_class pc left join pg_namespace pn on pc.relnamespace = pn.oid
                    where pn.nspname='hymn_view' and pc.relname = ?
                """, api
            ).size shouldBe 1
        }
    }
}