package github.afezeria.hymn.common.sql.field

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import mu.KLogging
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class CommonBeforeUpdateTriggerTest : BaseDbTest() {
    companion object : KLogging() {
        lateinit var objId: String
        lateinit var objSourceTable: String
        lateinit var objApi: String
        lateinit var typeId: String
        lateinit var obj: Map<String, Any?>

        lateinit var STANDARD_FIELD: Array<String>

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            adminConn.use {
                obj = createBObject()
                objId = obj["id"] as String
                objSourceTable = obj["source_table"] as String
                objApi = obj["api"] as String
                typeId = obj["type_id"] as String
                STANDARD_FIELD =
                    arrayOf(DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, typeId)
            }
        }

        @JvmStatic
        @AfterAll
        fun clear() {
            clearBObject()
        }

    }


    @Test
    fun `inactive fields cannot be modified`() {
        adminConn.use {
            var field = it.execute(
                """
                    insert into hymn.core_biz_object_field (active,biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (false,?, '文本字段', 'tfield', 'text', 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            it.execute(
                "update hymn.core_biz_object_field set active=false where id = ?",
                field["id"]
            )
            shouldThrow<PSQLException> {
                it.execute(
                    "update hymn.core_biz_object_field set name='测试' where id = ?",
                    field["id"]
                )
            }.apply {
                message shouldContain "[f:inner:03601] 字段停用时无法修改属性"
            }
        }
    }

    @Test
    fun `api property cannot be modified`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (active,biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (false,?, ${randomFieldNameAndApi("text")}, 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            shouldThrow<PSQLException> {
                it.execute(
                    "update hymn.core_biz_object_field set api='ccc' where id = ?",
                    field["id"]
                )
            }.apply {
                message shouldContain "[f:inner:00500] 不能修改api"
            }
        }
    }

    @Test
    fun `type property cannot be modified`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (active,biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (false,?, ${randomFieldNameAndApi("text")}, 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            shouldThrow<PSQLException> {
                it.execute(
                    "update hymn.core_biz_object_field set type='bbb' where id = ?",
                    field["id"]
                )
            }.apply {
                message shouldContain "[f:inner:03700] 不能修改字段类型"
            }
        }
    }

    @Test
    fun `field cannot be deactivated when it is referenced`() {
        val master = createBObject()
        val masterId = master["id"] as String
        try {
            adminConn.use {
                val fieldId = it.execute(
                    """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'整数','intfield','integer',50000,50,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )[0]["id"]
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','masterfield','master_slave',?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, masterId, *COMMON_INFO
                )[0]

                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'汇总','summaryfield','summary',?,?,'max',0,?,?,?,?,now(),now()) returning *;
                    """,
                    masterId, objId, fieldId, *COMMON_INFO
                )[0]
                val e = shouldThrow<PSQLException> {
                    it.execute(
                        "update hymn.core_biz_object_field set active = false where id = ?",
                        fieldId
                    )
                }
                e.message shouldContain "\\[f:inner:04400\\] 当前字段被汇总字段 .*? 引用，不能停用".toRegex()
            }
        } finally {
            deleteBObject(masterId)
        }
    }

}