package com.github.afezeria.hymn.common.sql.core.field

import com.github.afezeria.hymn.common.BaseDbTest
import com.github.afezeria.hymn.common.COMMON_INFO
import com.github.afezeria.hymn.common.customBizObject
import com.github.afezeria.hymn.common.userConn
import com.github.afezeria.hymn.common.util.execute
import com.github.afezeria.hymn.common.util.randomUUIDStr
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class SelectFieldTest : BaseDbTest() {
    @Test
    fun `required field is null`() {
        customBizObject {
            assertSoftly {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("select")},'0',1,null,?,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        randomUUIDStr(),
                        *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:06200] 可选个数/可见行数/字典项不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("select")},'0',null,1,?,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        randomUUIDStr(),
                        *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:06200] 可选个数/可见行数/字典项不能为空"

                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("select")},'0',1,1,?,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        null,
                        *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:06200] 可选个数/可见行数/字典项不能为空"

            }
        }
    }

    @Test
    fun `option_number must be greater than or equal to 0`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("select")},'0',1,0,?,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, randomUUIDStr(), *COMMON_INFO
                )

            }.message shouldContain "[f:inner:06300] 可选个数必须大于0"
        }
    }

    @Test
    fun `visible_row must be greater than or equal to 0`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("select")},'0',0,1,?,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, randomUUIDStr(), *COMMON_INFO
                )
            }.message shouldContain "[f:inner:06400] 可见行数必须大于0"
        }
    }

    @Test
    fun `dict not exists`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("select")},'0',1,1,?,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, randomUUIDStr(), *COMMON_INFO
                )
            }.message shouldContain "\\[f:inner:06500\\] 字典 \\[id:\\w+\\] 不存在".toRegex()
        }
    }

    @Test
    fun success() {
        customBizObject {

            val dict = it.execute(
                """
                    insert into hymn.core_dict  ( name, api,  create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values ('测试字典','test_dict5',?,?,?,?,now(),now()) returning *;
                    """,
                *COMMON_INFO
            )[0]
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("select")},'0',1,1,?,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                dict["id"],
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "text"
            it.execute(
                "update hymn.core_dict  set field_id = ? where id = ?",
                field["id"], dict["id"]
            )

            it.fieldShouldExists(field["api"])

            userConn.use {
                it.execute(
                    """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                    *standardField, "0"
                ).size shouldBe 1
            }
            it.execute(
                "update hymn.core_biz_object_field set active = false where id = ?",
                field["id"]
            )
            it.execute("delete from hymn.core_biz_object_field where id = ?", field["id"])
            it.execute("delete from hymn.core_dict where id = ?", dict["id"])
            //            字段删除后自动删除专属字典

            it.execute(
                "select * from hymn.core_dict where id = ?",
                dict["id"]
            ).size shouldBe 0

        }
    }
}
