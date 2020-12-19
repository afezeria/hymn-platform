package github.afezeria.hymn.common.sql.field

import github.afezeria.hymn.common.adminConn
import github.afezeria.hymn.common.sql.*
import github.afezeria.hymn.common.userConn
import github.afezeria.hymn.common.util.execute
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

/**
 * @author afezeria
 */
class TriggerAutoGenerateTest:BaseDbTest() {
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

    @Test
    fun `multi field test of mref_trigger `() {
//        测试多选字段触发器能同时作用于多个多选字段
        val refObj1 = createBObject()
        val refObj2 = createBObject()
        val refId1 = refObj1["id"] as String
        val refId2 = refObj2["id"] as String
        val fieldApi1: String
        val fieldApi2: String
        try {
            adminConn.use {
                var field = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        ref_list_label,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'多选关联对象1','mreffield1','mreference',?,'restrict','从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, refId1, *COMMON_INFO
                )[0]
                (field["source_column"] as String) shouldStartWith "mref"
                fieldApi1 = field["api"] as String
                field = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        ref_list_label,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'多选关联对象2','mreffield2','mreference',?,'restrict','从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, refId2, *COMMON_INFO
                )[0]
                (field["source_column"] as String) shouldStartWith "mref"
                fieldApi2 = field["api"] as String
            }
            userConn.use {
                val uuids = (1..5).map { randomUUIDStr() }
                val refDataIds = uuids.joinToString(";")
                var insData = it.execute(
                    """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${fieldApi1},${fieldApi2}) 
                        values (now(), now(), ?, ?, ?, ?, ?, ?) returning *;""",
                    *STANDARD_FIELD, refDataIds,refDataIds
                )
                insData.size shouldBe 1
                val dataId = insData[0]["id"] as String
//                关联表1
                var data = it.execute(
                    """
                        select * from hymn_view.join_${objApi}_${fieldApi1};
                    """
                )
                data.size shouldBe 5
                data[0]["s_id"] shouldBe dataId
                data.map { it["t_id"] } shouldContainAll uuids

//                关联表2
                data = it.execute(
                    """
                        select * from hymn_view.join_${objApi}_${fieldApi2};
                    """
                )
                data.size shouldBe 5
                data[0]["s_id"] shouldBe dataId
                data.map { it["t_id"] } shouldContainAll uuids
            }
        } finally {
            deleteBObject(refId1)
            deleteBObject(refId2)
        }
    }
}