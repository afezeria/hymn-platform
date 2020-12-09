package github.afezeria.hymn.common.sql.field

import github.afezeria.hymn.common.adminConn
import github.afezeria.hymn.common.sql.*
import github.afezeria.hymn.common.userConn
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import mu.KLogging
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class UpdateTriggerBranchTest : BaseDbTest() {
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

    @AfterEach
    fun clearField() {
        adminConn.use {
            it.execute(
                """
                    update hymn.core_b_object_field set active=false 
                    where is_predefined = false
                    and active=true
                    and object_id = ?
                """, objId
            )
            it.execute(
                " delete from hymn.core_b_object_field where active = false and object_id = ? ",
                objId
            )
        }
    }


    @Test
    fun `inactive fields cannot be modified`() {
        adminConn.use {
            var field = it.execute(
                """
                    insert into hymn.core_b_object_field (active,object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (false,?, '文本字段', 'tfield', 'text', 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            it.execute("update hymn.core_b_object_field set active=false where id = ?", field["id"])
            field = it.execute(
                "update hymn.core_b_object_field set name='测试' where id = ? returning *",
                field["id"]
            )[0]
            field["name"] shouldBe "文本字段"
        }
    }

    @Test
    fun `cannot modify api properties`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field (active,object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (false,?, '文本字段', 'tfield', 'text', 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            val e = shouldThrow<PSQLException> {
                it.execute(
                    "update hymn.core_b_object_field set type='bbb' where id = ?",
                    field["id"]
                )
            }
            e.message shouldContain "不能修改字段类型"
        }
    }

    @Test
    fun `the field cannot be enabled after the associated object has been deleted`() {
        val master = createBObject()
        val masterId = master["id"] as String
        try {
            adminConn.use {
                val fieldId = it.execute(
                    """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','masterfield','master_slave',?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, masterId, *COMMON_INFO
                )[0]["id"] as String
                it.execute(
                    "update hymn.core_b_object_field set active=false where id = ?",
                    fieldId
                )
                deleteBObject(masterId)
                val e = shouldThrow<PSQLException> {
                    it.execute(
                        "update hymn.core_b_object_field set active=true where id = ?",
                        fieldId
                    )
                    println()
                }
                e.message shouldContain "引用对象已删除，不能启用字段"
                println()
            }
        } finally {
            deleteBObject(masterId)
        }
    }

    @Test
    fun `cannot change other properties when a field is enabled`() {
        adminConn.use {
            var field = it.execute(
                """
                    insert into hymn.core_b_object_field (object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, '文本字段', 'tfield', 'text', 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            it.execute("update hymn.core_b_object_field set active=false where id = ?", field["id"])
            field = it.execute(
                "update hymn.core_b_object_field set active =true , name='测试' where id = ? returning *",
                field["id"]
            )[0]
            field["active"] shouldBe true
            field["name"] shouldBe "文本字段"
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
                    insert into hymn.core_b_object_field  ( object_id, name, api, type,  max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'整数','intfield','integer',50000,50,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )[0]["id"]
                val relField = it.execute(
                    """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','masterfield','master_slave',?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, masterId, *COMMON_INFO
                )[0]

                val summary = it.execute(
                    """
                    insert into hymn.core_b_object_field (object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'汇总','summaryfield','summary',?,?,'max',0,?,?,?,?,now(),now()) returning *;
                    """,
                    masterId, objId, fieldId, *COMMON_INFO
                )[0]
                val e = shouldThrow<PSQLException> {
                    it.execute(
                        "update hymn.core_b_object_field set active = false where id = ?",
                        fieldId
                    )
                }
                e.message shouldContain "当前字段被汇总字段： .*? 引用，不能停用".toRegex()
            }
        } finally {
            deleteBObject(masterId)
        }
    }

    @Test
    fun `rebuild view`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field (active,object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (false,?, '文本字段', 'tfield', 'text', 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            val fieldId = field["id"] as String
            it.execute("update hymn.core_b_object_field set active=false where id = ?", fieldId)
            it.execute(
                """
                    select * from pg_attribute pa left join pg_class pc on pa.attrelid=pc.oid
                    left join pg_namespace pn on pc.relnamespace = pn.oid
                    where pn.nspname='hymn_view'
                    and pc.relname=?
                    and pa.attname=?
                """,
                objApi, field["api"]
            ).size shouldBe 0
            it.execute("update hymn.core_b_object_field set active=true where id = ?", fieldId)
            it.execute(
                """
                    select * from pg_attribute pa left join pg_class pc on pa.attrelid=pc.oid
                    left join pg_namespace pn on pc.relnamespace = pn.oid
                    where pn.nspname='hymn_view'
                    and pc.relname=?
                    and pa.attname=?
                """,
                objApi, field["api"]
            ).size shouldBe 1
        }
    }

    @Test
    fun `dynamically create history triggers`() {
        val field: MutableMap<String, Any?>
        adminConn.use {
            field = it.execute(
                """
                    insert into hymn.core_b_object_field (active,object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (false,?, '文本字段', 'tfield', 'text', 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
        }
        val data: MutableMap<String, Any?>
        userConn.use {
            data = it.execute(
                """
                    insert into hymn_view.${objApi} (owner_id,create_by_id,modify_by_id,type_id,
                        create_date,modify_date,tfield__cf)
                    values (?,?,?,?,now(),now(),'abc') returning *
                """,
                DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, typeId,
            )[0]
            it.execute("update hymn_view.${objApi} set tfield__cf='def' where id = ?", data["id"])
        }
        adminConn.use {
            it.execute(
                "select * from hymn.${obj["source_table"]}_history"
            ).size shouldBe 0
            it.execute("update hymn.core_b_object_field set history=true where id = ?", field["id"])
        }
        userConn.use {
            it.execute("update hymn_view.${objApi} set tfield__cf='123' where id = ?", data["id"])
        }
        adminConn.use {
            it.execute(
                "select * from hymn.${obj["source_table"]}_history"
            ).size shouldBe 1
        }
    }


}