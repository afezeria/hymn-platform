package github.afezeria.hymn.common.sql.trigger

import github.afezeria.hymn.common.BaseDbTest
import github.afezeria.hymn.common.COMMON_INFO
import github.afezeria.hymn.common.customBizObject
import github.afezeria.hymn.common.sql.field.randomFieldNameAndApi
import github.afezeria.hymn.common.util.execute
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

/**
 * @author afezeria
 */
class DataTableHistoryTriggerTest : BaseDbTest() {
    @Test
    fun `soft delete data`() {
        customBizObject {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (history,biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (true,?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            val data = it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *standardField, "abc"
            )[0]
            val dataId = data["id"] as String
            it.execute("update hymn_view.${objApi} set deleted = true")
            it.execute(
                "select * from hymn_view.${objApi}_history where id = ?",
                dataId
            ).apply {
                size shouldBe 1
                get(0)["operation"] shouldBe "sd"
            }
        }
    }

    @Test
    fun `recover data`(){
        customBizObject {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (history,biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (true,?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            val data = it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *standardField, "abc"
            )[0]
            val dataId = data["id"] as String
            it.execute("update hymn_view.${objApi} set deleted = true")
            it.execute("update hymn_view.${objApi} set deleted = false")
            it.execute(
                "select * from hymn_view.${objApi}_history where id = ? and operation = 'r'",
                dataId
            ).size shouldBe 1
        }
    }
}