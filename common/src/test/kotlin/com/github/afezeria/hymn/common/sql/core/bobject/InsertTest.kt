package com.github.afezeria.hymn.common.sql.core.bobject

import com.github.afezeria.hymn.common.BaseDbTest
import com.github.afezeria.hymn.common.COMMON_INFO
import com.github.afezeria.hymn.common.adminConn
import com.github.afezeria.hymn.common.clearBObject
import com.github.afezeria.hymn.common.util.execute
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

/**
 * @author afezeria
 */
class InsertTest : BaseDbTest() {
    companion object {
        lateinit var id: String

        @AfterAll
        @JvmStatic
        fun clear() {
            clearBObject()
        }
    }

    @Test
    fun insert() {
        adminConn.use {
            val insert = it.execute(
                """
                insert into hymn.core_biz_object(type,name,api,create_by_id,create_by,modify_by_id,
                    modify_by,create_date,modify_date)
                values ('custom','测试对象','test_obj',?,?,?,?,now(),now()) returning *;
                """,
                *COMMON_INFO
            )[0]
            val api = insert["api"] as String
            val sourceTable = insert["source_table"] as String
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

        }
    }

}