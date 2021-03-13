package com.github.afezeria.hymn.common.sql.core.field

import com.github.afezeria.hymn.common.*
import com.github.afezeria.hymn.common.util.execute
import com.github.afezeria.hymn.common.util.randomUUIDStr
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotContain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class CommonBeforeInsertTriggerTest : BaseDbTest() {
    companion object {
        lateinit var objId: String
        lateinit var objSourceTable: String
        lateinit var objApi: String
        lateinit var typeId: String
        lateinit var remoteObjId: String

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

    }

    @Test
    fun `throw if api is illegal`() {
        adminConn.use {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'文本字段j52','text','text',255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )[0]
            }.apply {
                sqlState shouldBe "P0001"
                message shouldContain "[f:inner:01000] 无效的 api, text 是数据库关键字"
            }
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
                    insert into hymn.core_biz_object_field (predefined,biz_object_id, name, api, type, max_length, min_length, 
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
                    insert into hymn.core_biz_object_field (predefined,source_column,biz_object_id, name, api, type, max_length, min_length, 
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

    @Test
    fun `predefined field type cannot be mreference or summary or master_slave`() {
        adminConn.use {
            listOf("mreference", "summary", "master_slave").forEach { type ->
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (predefined,source_column,biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (true,'abc',?,${randomFieldNameAndApi(type)},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )[0]
                }.apply {
                    sqlState shouldBe "P0001"
                    message shouldContain "[f:inner:03501] 预定义字段类型不能为 多选关联/汇总/主从"
                }
            }
        }
    }

    @Test
    fun `field type check`() {
        adminConn.use {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'测试字段','test242','double',255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )[0]
            }.apply {
                sqlState shouldBe "P0001"
                message shouldContain "[f:inner:01300] 未知的字段类型 double"
            }
        }
    }

    @Test
    fun `when the fields of the custom object are created, the api property will be suffixed and the source property will be set`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("text")}, 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            field["api"] as String shouldMatch ".*?__cf$".toRegex()
            field["source_column"] as String shouldMatch "\\w+\\d{3}".toRegex()
        }
    }

    @Test
    fun `an exception is thrown when the number of fields reaches the maximum`() {
        val master = createBObject()
        val masterId = master["id"] as String
        try {
            adminConn.use {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (ref_id,ref_list_label, ref_delete_policy, 
                        biz_object_id, name, api, type, 
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'子对象','cascade',?,${randomFieldNameAndApi("master_slave")},  ?, ?, ?, ?, now(), now()) returning *;
                    """,
                    masterId, objId, *COMMON_INFO
                )[0]
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (ref_id,ref_list_label,biz_object_id, name, api, type, 
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'子对象',?,${randomFieldNameAndApi("master_slave")}, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                        masterId, objId, *COMMON_INFO
                    )[0]
                }.apply {
                    sqlState shouldBe "P0001"
                    message shouldContain "[f:inner:03600] 主从类型字段的数量已达到上限"
                }
            }
        } finally {
            deleteBObject(masterId)
        }
    }
}
