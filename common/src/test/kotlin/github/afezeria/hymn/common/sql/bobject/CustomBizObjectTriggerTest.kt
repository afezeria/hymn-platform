package github.afezeria.hymn.common.sql.bobject

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldMatch
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException
import java.time.LocalDateTime

/**
 * @author afezeria
 */
class CustomBizObjectTriggerTest : BaseDbTest() {
    companion object {
        lateinit var id: String

        @AfterAll
        @JvmStatic
        fun clear() {
            clearBObject()
        }
    }

    @Test
    fun `create custom object`() {
        val api: String
        adminConn.use {
            val insert = createBObject()

            api = insert["api"] as String
            api shouldEndWith "__co"
            val sourceTable = insert["source_table"] as String
            sourceTable shouldMatch "^core_data_table_\\d{3}"
//            存在历史记录触发器
            it.execute(
                """
                    select * from information_schema.triggers t
                    where event_object_schema='hymn' and event_object_table='${sourceTable}'
                    and trigger_name ='a99_${sourceTable}_history'
                """
            ).run {
                size shouldBe 2
                map { it["event_manipulation"] } shouldContainAll listOf("UPDATE", "DELETE")
            }

//            占用一个数据表
            it.execute(
                """
                select * from hymn.core_table_obj_mapping 
                where table_name=? and obj_api=?
                """,
                sourceTable, api
            ).size shouldBe 1

//            存在视图
            it.execute(
                """
                    select * from pg_class pc
                    left join pg_namespace pn on pn.oid = pc.relnamespace
                    where pn.nspname='hymn_view' and pc.relname=? and pc.relkind='v'
                """, api
            ).size shouldBe 1

//            存在共享表
            it.execute(
                """
                    select * from pg_class pc
                    left join pg_namespace pn on pn.oid = pc.relnamespace
                    where pn.nspname='hymn_view' and pc.relname=? and pc.relkind='r'
                """, api + "_share"
            ).size shouldBe 1
        }
        userConn.use {
            val shareTable = api + "_share"
//            已给hymn_user角色授予共享表 select,insert,delete 权限
            var dataId = randomUUIDStr()
            val accountId = randomUUIDStr()
            it.execute(
                """
                    insert into hymn_view.${shareTable} (data_id,account_id) values (?,?);
                """, dataId, accountId
            )
            val sharedData = it.execute(
                """
                    select * from hymn_view.${shareTable};
                """
            ).apply {
                size shouldBe 1
                get(0)
            }
            shouldThrow<PSQLException> {
                it.execute(
                    """
                        update hymn_view.${shareTable} set account_id = ? where data_id=? and account_id=?
                    """, randomUUIDStr(), dataId, accountId
                )
            }.sqlState shouldBe "42501"
            it.execute(
                """
                    delete from hymn_view.${shareTable} where data_id=? and account_id=?
                """, dataId, accountId
            )

//            已给hymn_user角色授予对象视图 select,insert,update,delete 权限
            dataId = it.execute(
                """
                    insert into hymn_view.${api} (owner_id,create_by_id,modify_by_id,type_id,
                        create_date,modify_date) 
                    values (?,?,?,?,?,?) returning id;
                """, randomUUIDStr(), randomUUIDStr(), randomUUIDStr(), randomUUIDStr(),
                LocalDateTime.now(), LocalDateTime.now()
            )[0]["id"] as String
            it.execute(
                """
                    select * from hymn_view.${api}
                """
            ).size shouldBe 1
            val newId = randomUUIDStr()
            it.execute(
                """
                    update hymn_view.${api} set owner_id = ? where id = ?
                """, newId, dataId
            )
            it.execute(
                """
                    delete from hymn_view.${api} where id=?
                """, dataId
            )
            it.execute(
                """
                    select * from hymn_view.${api}
                """
            ).size shouldBe 0

//            历史记录触发器记录一条删除数据
            it.execute(
                """
                    select * from hymn_view.${api + "_history"}
                """
            ).apply {
                size shouldBe 1
                get(0)["id"] shouldBe dataId
                get(0)["operation"] shouldBe "d"
            }
        }
    }


}