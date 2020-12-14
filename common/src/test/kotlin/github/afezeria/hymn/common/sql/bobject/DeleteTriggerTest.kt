package github.afezeria.hymn.common.sql.bobject

import github.afezeria.hymn.common.adminConn
import github.afezeria.hymn.common.sql.*
import github.afezeria.hymn.common.userConn
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class DeleteTriggerTest : BaseDbTest() {

    @AfterEach
    fun clear() {
        clearBObject()
    }

    @Test
    fun `cannot delete active object`() {
        val obj = createBObject()
        val id = obj["id"] as String
        adminConn.use {
            shouldThrow<PSQLException> {
                it.execute("delete from hymn.core_biz_object where id=?", id)
            }.message shouldContain "无法删除启用的对象"
        }
    }

    @Test
    fun delete() {
        val obj = createBObject()
        val id = obj["id"] as String
        val api = obj["api"] as String
        val sourceTable = obj["source_table"] as String
        val typeId = obj["type_id"] as String
        val obj2 = createBObject()
        val refFieldId: String
        adminConn.use {
//            创建关联表
            val refField = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        ref_list_label,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'多选关联对象','mreffield','mreference',?,'restrict','从对象',?,?,?,?,now(),now()) returning *;
                    """,
                obj2["id"], id, *COMMON_INFO
            )[0]
            refFieldId = refField["id"] as String
//            创建字段
            it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'文本字段','tfield','text',255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                id, *COMMON_INFO
            )[0]
        }
        userConn.use {
//            往数据表和历史表中添加数据
            val dataId = it.execute(
                """
                insert into hymn_view.${api} (create_date,modify_date,owner_id,create_by_id,
                    modify_by_id,type_id) 
                values (now(), now(), ?, ?, ?, ?) returning id;""",
                DEFAULT_ACCOUNT_ID,
                DEFAULT_ACCOUNT_ID,
                DEFAULT_ACCOUNT_ID,
                typeId,
            )[0]["id"]
            it.execute("delete from hymn_view.${api} where id=?", dataId)
            it.execute(
                """
                insert into hymn_view.${api} (create_date,modify_date,owner_id,create_by_id,
                    modify_by_id,type_id) 
                values (now(), now(), ?, ?, ?, ?) returning id;""",
                DEFAULT_ACCOUNT_ID,
                DEFAULT_ACCOUNT_ID,
                DEFAULT_ACCOUNT_ID,
                typeId,
            )
        }
        adminConn.use {
            it.execute("update hymn.core_biz_object_field set active=false where id=?", refFieldId)
            it.execute("select * from hymn_view.${api}").size shouldBeGreaterThan 0
            it.execute("select * from hymn_view.${api}_history").size shouldBeGreaterThan 0
//            存在递增序列
            it.execute(
                """
                    select * from information_schema.sequences s
                    where sequence_schema='hymn_view' and sequence_name='${api}_auto_number_seq'
                """
            ).size shouldBe 1
//            存在引用字段
            it.execute(
                "select * from hymn.core_biz_object_field where ref_id = ?",
                id
            ).size shouldBe 1
//            存在触发器
            it.execute(
                """
                    select * from information_schema.triggers t
                    where event_object_schema='hymn' and event_object_table='${sourceTable}'
                """
            ).size shouldBeGreaterThan 0

//            占用一个数据表
            it.execute(
                """
                select * from hymn.core_table_obj_mapping 
                where table_name=? and obj_api=?
                """,
                sourceTable, api
            ).size shouldBe 1
//            占用部分列
            it.execute(
                """
                    select * from hymn.core_column_field_mapping 
                    where table_name=? and field_api is not null;
                """, sourceTable
            ).size shouldBeGreaterThan 0


//            停用并删除对象
            it.execute("update hymn.core_biz_object set active=false where id=?", id)
            it.execute(
                """
                    select * from information_schema.tables t 
                    where table_name = '${api}' and table_schema = 'hymn_view'
                """
            ).size shouldBe 0
            it.execute("delete from hymn.core_biz_object where id=?", id)

//            数据表已清空
            it.execute("select * from hymn.${sourceTable}").size shouldBe 0
//            历史记录表已删除
            it.execute(
                """
                    select * from information_schema.tables t 
                    where table_name = '${api}_history' and table_schema = 'hymn_view'
                """
            ).size shouldBe 0
//            序列已删除
            it.execute(
                """
                    select * from information_schema.sequences s
                    where sequence_schema='hymn_view' and sequence_name='${api}_auto_number_seq'
                """
            ).size shouldBe 0
//            关联字段已删除
            it.execute(
                "select * from hymn.core_biz_object_field where ref_id = ?",
                id
            ).size shouldBe 0
//            触发器已删除
            it.execute(
                """
                    select * from information_schema.triggers t
                    where event_object_schema='hymn' and event_object_table='${sourceTable}'
                """
            ).size shouldBe 0

//            表资源已归还
            it.execute(
                """
                select * from hymn.core_table_obj_mapping 
                where table_name=? and obj_api is null
                """,
                sourceTable
            ).size shouldBe 1
//            列资源已归还
            it.execute(
                """
                    select * from hymn.core_column_field_mapping 
                    where table_name=? and field_api is not null;
                """, sourceTable
            ).size shouldBe 0


        }
    }
}