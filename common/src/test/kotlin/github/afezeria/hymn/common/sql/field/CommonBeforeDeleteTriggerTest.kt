package github.afezeria.hymn.common.sql.field

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import mu.KLogging
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException
import java.time.LocalDateTime

/**
 * @author afezeria
 */
class CommonBeforeDeleteTriggerTest : BaseDbTest() {
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
    fun `field cannot be deleted when it is active`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            shouldThrow<PSQLException> {
                it.execute("delete from hymn.core_biz_object_field where id = ?", field["id"])
            }.apply {
                sqlState shouldBe "P0001"
                message shouldContain "\\[f:inner:04500\\] 字段 \\w+ 未停用，无法删除".toRegex()
            }
        }
    }

    @Test
    fun `clear data when deleting fields of custom objects`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            val data = it.execute(
                """
                insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                    modify_by_id,type_id,${field["api"]}) 
                values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, "aaa",
            )[0]
            val dataId = data["id"] as String
            it.execute(
                "update hymn.core_biz_object_field set active=false where id = ?",
                field["id"]
            )
            it.execute("delete from hymn.core_biz_object_field where id = ?", field["id"])
            val res = it.execute(
                "select ${field["source_column"]} from hymn.${obj["source_table"]} where id = ?",
                dataId
            )[0]
            res shouldContain (field["api"] as String to null)
        }
    }

    @Test
    fun `data is not processed when fields of module objects are deleted`() {
        adminConn.use {
            it.execute(
                """
                    create table hymn.module_object_1(
                    id text,
                    idx int
                    )
                """
            )
            val obj = it.execute(
                """
                insert into hymn.core_biz_object(type,source_table,name,api,active,can_insert,can_update,can_delete,create_by_id,
                    create_by,modify_by_id,modify_by,create_date,modify_date)
                values ('module','module_object_1','测试对象','test_obj${objSeq.nextInt()}',true,?,?,?,?,?,?,?,?,?) returning *;
                """,
                true, true, true,
                *COMMON_INFO, LocalDateTime.now(), LocalDateTime.now()
            )[0]
            val objId = obj["id"] as String
            val objApi = obj["api"] as String
            try {
                val field = it.execute(
                    """
                    insert into hymn.core_biz_object_field  (is_predefined,source_column, biz_object_id, name, api, type,  max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (true,'idx',?,${randomFieldNameAndApi("integer")},50000,50,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )[0]
                val fieldId = field["id"] as String
                val fieldApi = field["api"] as String
                it.execute(
                    """
                    insert into hymn_view.${objApi} (${fieldApi}) values (2);
                """
                )
                it.execute(
                    "update hymn.core_biz_object_field set active = false where id = ?",
                    fieldId
                )
                it.execute("delete from hymn.core_biz_object_field where id = ?", fieldId)
//                字段被删除后原始表中列的数据没有被清空
                it.execute(
                    "select idx from hymn.module_object_1"
                )[0]["idx"] shouldNotBe null
            } finally {
                deleteBObject(objId)
                it.execute("drop table hymn.module_object_1");
            }

        }

    }

    @Test
    fun `return column resources when deleting fields of custom objects`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            it.execute(
                "update hymn.core_biz_object_field set active=false where id = ?",
                field["id"]
            )
            it.execute("delete from hymn.core_biz_object_field where id = ?", field["id"])
            it.execute(
                "select * from hymn.core_column_field_mapping where table_name = ? and column_name = ?",
                objSourceTable, field["source_column"]
            )[0] shouldContain (field["api"] as String to null)
        }
    }

    @Test
    fun `when deleting a field, delete the summary field that references the field`() {
        val master = createBObject()
        val masterId = master["id"] as String
        try {
            adminConn.use {
                val fieldId = it.execute(
                    """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  max_length, 
                        min_length, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("integer")},50000,50,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )[0]["id"]
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("master_slave")},?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, masterId, *COMMON_INFO
                )[0]

                val summaryField = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,?,'max',0,?,?,?,?,now(),now()) returning *;
                    """,
                    masterId, objId, fieldId, *COMMON_INFO
                )[0]
                it.execute(
                    "update hymn.core_biz_object_field set active = false where id = ?",
                    summaryField["id"]
                )
                it.execute(
                    "update hymn.core_biz_object_field set active = false where id = ?",
                    fieldId
                )
                it.execute("delete from hymn.core_biz_object_field where id = ?", fieldId)
//                汇总字段被删除
                it.execute(
                    "select * from hymn.core_biz_object_field where id = ?",
                    summaryField["id"]
                ).size shouldBe 0
                println()
            }
        } finally {
            deleteBObject(masterId)
        }
    }

}