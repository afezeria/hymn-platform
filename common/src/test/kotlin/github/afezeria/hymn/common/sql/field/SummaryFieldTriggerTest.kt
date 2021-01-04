package github.afezeria.hymn.common.sql.field

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import mu.KLogging
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class SummaryFieldTriggerTest:BaseDbTest() {
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
    fun `when a object is deleted, the field summarizing the object is also deleted`() {
        val slave = createBObject()
        val slaveId = slave["id"] as String
        adminConn.use {
            val masterSlaveField = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("master_slave")},?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                slaveId, objId, *COMMON_INFO
            )[0]
            val summaryField = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,'count',0,?,?,?,?,now(),now()) returning *;
                    """,
                objId,slaveId, *COMMON_INFO
            )[0]
            val summaryFieldId = summaryField["id"] as String
            it.execute(
                """
                    update hymn.core_biz_object_field set active = false where id = ?
                """, summaryFieldId
            )
            it.execute(
                """
                    update hymn.core_biz_object_field set active = false where id = ?
                """, masterSlaveField["id"]
            )
            it.execute(
                """
                    update hymn.core_biz_object set active = false where id=?
                """,slaveId
            )
//            删除对象
            it.execute(
                """
                    delete from hymn.core_biz_object where id = ?
                """, slaveId
            )
//            汇总字段被删除
            it.execute(
                """
                    select * from hymn.core_biz_object_field where id=?
                """,summaryFieldId
            ).size shouldBe 0
        }
    }

    @Test
    fun `summary field cannot be active when the target field of summary is inactive or deleted`() {
        val master = createBObject()
        val masterId = master["id"] as String
        try{
            adminConn.use {
                val masterSlaveField = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("master_slave")},?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, masterId, *COMMON_INFO
                )[0]
                val intField = it.execute(
                    """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("integer")},50000,50,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )[0]
                val intFieldId = intField["id"] as String
                val summaryField = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,?,'sum',0,?,?,?,?,now(),now()) returning *;
                    """,
                    masterId, objId, intFieldId, *COMMON_INFO
                )[0]
                val summaryFieldId = summaryField["id"] as String
                it.execute(
                    """
                    update hymn.core_biz_object_field set active = false where id = ?
                """, summaryFieldId
                )
                it.execute(
                    """
                    update hymn.core_biz_object_field set active = false where id = ?
                """, intFieldId
                )
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    update hymn.core_biz_object_field set active = true where id = ?
                """, summaryFieldId
                    )
                }.apply {
                    message shouldContain "[f:inner:04100] 汇总目标字段已停用，不能启用当前字段"
                }
                it.execute(
                    """
                    delete from hymn.core_biz_object_field where id = ?
                """, intFieldId
                )
                it.execute(
                    """
                    select * from hymn.core_biz_object_field where id=?
                """,summaryFieldId
                ).size shouldBe 0
            }
        }finally {
            deleteBObject(masterId)
        }
    }
}