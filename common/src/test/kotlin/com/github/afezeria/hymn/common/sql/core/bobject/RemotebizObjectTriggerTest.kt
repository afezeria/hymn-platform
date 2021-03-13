package com.github.afezeria.hymn.common.sql.core.bobject

import com.github.afezeria.hymn.common.BaseDbTest
import com.github.afezeria.hymn.common.COMMON_INFO
import com.github.afezeria.hymn.common.adminConn
import com.github.afezeria.hymn.common.objSeq
import com.github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class RemotebizObjectTriggerTest : BaseDbTest() {
    @Test
    fun `source table of the module object is missing the deleted field`() {
        adminConn.use {
            val seq = objSeq.nextInt()
            val sourceTable = "test_module_object_$seq"
            it.execute(
                """
                create table hymn.$sourceTable(
                    id           text primary key     default replace(public.uuid_generate_v4()::text, '-', ''),
                    name text,
                    atext text,
                    aint bigint,
                    afloat double precision,
                    adatetime timestamptz
                )
            """
            )
            try {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                        insert into hymn.core_biz_object(can_soft_delete,name,api,type,source_table,
                            create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
                        values (true,'模块对象${seq}','$sourceTable','module','$sourceTable',?,?,?,?,
                            now(),now()) returning *;
                        """,
                        *COMMON_INFO
                    )
                }.apply {
                    sqlState shouldBe "P0001"
                    message shouldContain "[f:inner:02401] 模块对象数据表中缺少 deleted 字段，can_soft_delete 不能为true"
                }
            } finally {
                it.execute("drop table hymn.$sourceTable cascade")
            }
        }
    }
}