package github.afezeria.hymn.common.sql.field

import com.fasterxml.jackson.module.kotlin.readValue
import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.common.util.mapper
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import mu.KLogging
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

/**
 * @author afezeria
 */
class CommonAfterUpdateTriggerTest : BaseDbTest() {
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
    fun `rebuild the view when the owning object is not a remote object and the active property changes`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            val fieldApi = field["api"] as String
            val fieldId = field["id"]
            it.fieldShouldExists("hymn_view", objApi, fieldApi)
            it.execute("update hymn.core_biz_object_field set active = false where id = ?", fieldId)
            it.fieldShouldNotExists("hymn_view", objApi, fieldApi)
            it.execute("update hymn.core_biz_object_field set active = true where id = ?", fieldId)
            it.fieldShouldExists("hymn_view", objApi, fieldApi)
        }
    }

    @Test
    fun `rebuild the history trigger when the owning object is not a remote object and the active property or history property changes`() {
        val fieldApi1: String
        val fieldApi2: String
        val fieldId1: String
        val fieldId2: String
        val dataId: String
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            fieldApi1 = field["api"] as String
            fieldId1 = field["id"] as String
            it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0].apply {
                fieldApi2 = this["api"] as String
                fieldId2 = this["id"] as String
            }
//            开始记录field字段变更
            it.execute(
                "update hymn.core_biz_object_field set history = true where id = ?",
                fieldId1
            )
            it.execute(
                "update hymn.core_biz_object_field set history = true where id = ?",
                fieldId2
            )
        }
        userConn.use {
            val data = it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${fieldApi1},${fieldApi2}) 
                        values (now(), now(), ?, ?, ?, ?, ?,?) returning *;""",
                *STANDARD_FIELD, "aaa", "111"
            )[0]
            dataId = data["id"] as String
            it.execute("update hymn_view.${objApi} set ${fieldApi1} = 'bbb' where id = ?", dataId)
//            field字段内容变更后有一条历史记录
            it.execute(
                "select * from hymn_view.${objApi}_history where id = ?",
                dataId
            ).apply {
                size shouldBe 1
                val changeStr = get(0)["change"] as String
                val change = mapper.readValue<Map<String, Any?>>(changeStr)
                change.size shouldBe 1
                change.keys shouldContain fieldApi1
            }
        }
//        停用field1字段后重建触发器不再记录field字段变更
        adminConn.use {
            it.execute(
                "update hymn.core_biz_object_field set active = false where id = ?",
                fieldId1
            )
        }
        userConn.use {
            it.execute(
                "update hymn_view.${objApi} set ${fieldApi2} = '222' where id = ?",
                dataId
            )
            it.execute(
                "select * from hymn_view.${objApi}_history where id = ? order by stamp desc limit 1",
                dataId
            ).apply {
                val changeStr = get(0)["change"] as String
                val change = mapper.readValue<Map<String, Any?>>(changeStr)
                change.size shouldBe 1
                change.keys shouldContainExactly setOf(fieldApi2)
            }
        }
//        启用field1字段后如果history为true则重建触发器，再次开始记录field1字段的变更
        adminConn.use {
            it.execute("update hymn.core_biz_object_field set active = true where id = ?", fieldId1)
        }
        userConn.use {
            it.execute(
                "update hymn_view.${objApi} set ${fieldApi1} = 'ccc',${fieldApi2} = '333' where id = ?",
                dataId
            )
            it.execute(
                "select * from hymn_view.${objApi}_history where id = ? order by stamp desc limit 1",
                dataId
            ).apply {
                val changeStr = get(0)["change"] as String
                val change = mapper.readValue<Map<String, Any?>>(changeStr)
                change.size shouldBe 2
                change.keys shouldContainExactly setOf(fieldApi1, fieldApi2)
            }
        }
    }
}