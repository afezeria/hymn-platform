package github.afezeria.hymn.common.sql.field

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException
import java.sql.Connection

/**
 * @author afezeria
 */
class CommonTriggerTest : BaseDbTest() {
    companion object {
        lateinit var objId: String
        lateinit var objSourceTable: String
        lateinit var objApi: String
        lateinit var typeId: String
        lateinit var remoteObjId: String

        lateinit var masterObjId: String
        lateinit var masterFieldId: String

        lateinit var STANDARD_FIELD: Array<String>

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            adminConn.use {
                val obj = createBObject()
                objId = obj["id"] as String
                objSourceTable = obj["source_table"] as String
                objApi = obj["api"] as String
                typeId = obj["type_id"] as String
                STANDARD_FIELD =
                    arrayOf(DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, typeId)

                val remoteObj = it.execute(
                    """
                insert into hymn.core_biz_object(name,api,type,create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
                values ('测试对象','remote_obj','remote',?,?,?,?,now(),now()) returning *;
                """,
                    *COMMON_INFO
                )[0]
                remoteObjId = remoteObj["id"] as String

            }
        }

        @JvmStatic
        @AfterAll
        fun clear() {
            clearBObject()
        }

        fun Connection.fieldShouldExists(fieldName: String) {
            execute(
                """
                select pc.relname,pa.attname
                from pg_class pc
                left join pg_attribute pa on pc.oid=pa.attrelid
                left join pg_namespace pn on pc.relnamespace = pn.oid
                where pc.relkind='v'
                and pn.nspname='hymn_view'
                and pc.relname=?
                and pa.attname=?
                """,
                objApi, fieldName + "__cf"
            ).size shouldBe 1
        }
    }

    @Test
    fun `the new field active property is always true`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (active,biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (false,?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            field["active"] shouldBe true
        }
    }

    @Test
    fun `throw an exception when the object of the new field does not exist`() {
        adminConn.use {
            val e = shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                    randomUUIDStr(), *COMMON_INFO
                )[0]
            }
            e.sqlState shouldContain "P0001"
            e.message shouldContain "\\[f:inner:00600\\] 对象 \\[id:\\w+\\] 不存在".toRegex()
        }
    }

    @Test
    fun `throw an exception when the object of the new field does not active`() {
        val inactiveObj = createBObject()
        val id = inactiveObj["id"] as String
        try {
            adminConn.use {
                it.execute(
                    """
                        update hymn.core_biz_object set active = false where id = ?
                    """, id
                )
                val e = shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                        id, *COMMON_INFO
                    )[0]
                }
                e.sqlState shouldContain "P0001"
                e.message shouldContain "\\[f:inner:00700\\] 对象 \\[id:\\w+\\] 已停用，不能新增/更新相关数据".toRegex()
            }
        } finally {
            deleteBObject(id)
        }
    }

    @Test
    fun `predefined field must set source_column`() {
        adminConn.use {
            val e = shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (is_predefined,biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (true,?,${randomFieldNameAndApi("text")}, 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )[0]
            }
            e.sqlState shouldBe "P0001"
            e.message shouldContain "[f:inner:03300] 预定义字段必须指定 source_column"
        }
    }

    @Test
    fun `the field of the remote object has no suffix and source_column property is an empty string`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("text")}, 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                remoteObjId, *COMMON_INFO
            )[0]
            field["api"] as String shouldNotContain "__cf$".toRegex()
            field["source_column"] shouldBe ""
        }
    }

    @Test
    fun `remote objects cannot create predefined fields`() {
        adminConn.use {
            val e = shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (is_predefined,source_column,biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (true,'textcolumn',?,'预定义字段','text_abc','text', 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                    remoteObjId, *COMMON_INFO
                )[0]
            }
            e.sqlState shouldBe "P0001"
            e.message shouldContain "[f:inner:03400] 远程对象不能创建预定义字段"
        }
    }

    @Test
    fun `remote object cannot create a field of type picture or files or mreference or summary or master_slave`() {
        adminConn.use {
            listOf("picture", "files", "mreference", "summary", "master_slave").forEach { type ->
                val e = shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi(type)}, 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                        remoteObjId, *COMMON_INFO
                    )[0]
                }
                e.sqlState shouldBe "P0001"
                e.message shouldContain "[f:inner:03500] 远程对象不能创建 图片/文件/多选关联/汇总/主从 字段"
            }
        }
    }
}
