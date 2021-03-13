package com.github.afezeria.hymn.common.sql.core.field

import com.github.afezeria.hymn.common.*
import com.github.afezeria.hymn.common.util.execute
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

/**
 * @author afezeria
 */
class CommonAfterInsertTriggerTest : BaseDbTest() {
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
    fun `do not modify or create views when creating new fields for remote objects`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                remoteObjId, *COMMON_INFO
            )[0]
            it.fieldShouldNotExists("hymn", objApi, field["api"] as String)
        }
    }

    @Test
    fun `if the source_column of the new field starts with pl_ and the predefined is false, the view will not be modified or created`() {
        val master = createBObject()
        val masterId = master["id"] as String
        try {
            adminConn.use {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,ref_delete_policy,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','masterfield','master_slave',?,'从对象','cascade',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, masterId, *COMMON_INFO
                )[0]
                val field = it.execute(
                    """
                    insert into hymn.core_biz_object_field (s_id,s_type,min_length,biz_object_id, name, api, type,
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'count',1,?,${randomFieldNameAndApi("summary")},?,?,?,?,now(),now()) returning *;
                    """,
                    objId, masterId, *COMMON_INFO
                )[0]
                it.fieldShouldNotExists("hymn", objApi, field["api"] as String)
            }
        } finally {
            deleteBObject(masterId)
        }
    }
}