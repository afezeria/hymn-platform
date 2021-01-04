package github.afezeria.hymn.common.sql.field

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class InsertTriggerTest : BaseDbTest() {
    companion object {
        lateinit var objId: String
        lateinit var objSourceTable: String
        lateinit var objApi: String
        lateinit var typeId: String

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
                    update hymn.core_biz_object_field set active=false 
                    where is_predefined = false
                    and active=true
                    and biz_object_id = ?
                """, objId
            )
            it.execute(
                " delete from hymn.core_biz_object_field where active = false and biz_object_id = ? ",
                objId
            )
        }
    }


    @Test
    fun `add suffix __cf to custom fields of non remote objects`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (active,biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (false,?, '文本字段', 'tfield', 'text', 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            field["api"] as String shouldContain "__cf$".toRegex()
        }
    }

    @Test
    fun `the number of specified type fields exceeds the limit`() {
        val master = createBObject()
        try {
            adminConn.use {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','masterfield','master_slave',?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, master["id"], *COMMON_INFO
                )
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','masterfield','master_slave',?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, master["id"], *COMMON_INFO
                    )
                }.message shouldContain "主从类型字段的数量已达到上限"
            }
        } finally {
            deleteBObject(master["id"] as String)
        }
    }

    @Test
    fun `views are not created when remote objects create fields`() {
        adminConn.use {
            val obj = it.execute(
                """
                insert into hymn.core_biz_object(name,api,type,create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
                values ('测试对象','remote_obj','remote',?,?,?,?,now(),now()) returning *;
                """,
                *COMMON_INFO
            )[0]
            val objId = obj["id"] as String
            try {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, '文本字段', 'tfield', 'text', 255, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                    objId, *COMMON_INFO
                ).size shouldBe 1
                it.execute(
                    """
                    select * from pg_class pc left join pg_namespace pn on pn.oid = pc.relnamespace 
                    where pn.nspname='hymn_view'
                    and pc.relkind='v'
                    and pc.relname='remote_obj__cr'
                """
                ).size shouldBe 0
            } finally {
                deleteBObject(objId)
            }
        }
    }
}
