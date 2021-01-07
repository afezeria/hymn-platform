package github.afezeria.hymn.common.sql.field

import github.afezeria.hymn.common.BaseDbTest
import github.afezeria.hymn.common.COMMON_INFO
import github.afezeria.hymn.common.customBizObject
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class TextFieldTest : BaseDbTest() {

    @Test
    fun `required field is null`() {
        customBizObject {
            assertSoftly {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, '文本字段', 'tfield', 'text', null, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:05100] 最小长度、最大长度和显示行数都不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, '文本字段', 'tfield', 'text', 255, null, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:05100] 最小长度、最大长度和显示行数都不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, '文本字段', 'tfield', 'text', 255, 1, null, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:05100] 最小长度、最大长度和显示行数都不能为空"
            }
        }
    }

    @Test
    fun `min_length must be greater than or equal to 0`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, ${randomFieldNameAndApi("text")}, 255, -1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )
            }.message shouldContain "[f:inner:05200] 最小长度必须大于等于0"
        }
    }

    @Test
    fun `max_length must be less than or equal to 50000`() {
        customBizObject {

            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, ${randomFieldNameAndApi("text")}, 500000, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )
            }.message shouldContain "[f:inner:05300] 最大长度必须小于等于50000"
        }
    }

    @Test
    fun `max_length must be greater than or equal to min_length`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, ${randomFieldNameAndApi("text")}, 40, 50, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )
            }.message shouldContain "[f:inner:05400] 最小长度必须小于等于最大长度"
        }
    }

    @Test
    fun `visible_row must be greater than or equal to 0`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, ${randomFieldNameAndApi("text")}, 255, 1, 0, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )
            }.message shouldContain "[f:inner:05500] 可见行数必须大于等于1"
        }
    }

    @Nested
    inner class NameField {
        @Test
        fun `max_length must be less than or equal to 50000`() {
            customBizObject {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (source_column,predefined,biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values ('text',true,?, '编号','name','text', 256, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                    println()
                }.message shouldContain "[f:inner:05600] name字段最大长度必须小于等于255"
            }
        }

        @Test
        fun `standard_type must be 'name'`() {
            customBizObject {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (predefined,standard_type,biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (true,'abc',?,'编号','name','text', 200, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:03300] 预定义字段必须指定 source_column"
            }
        }
    }

    @Test
    fun success() {
        customBizObject {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "text"
            it.fieldShouldExists(field["api"])
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *standardField, "abc"
            ).size shouldBe 1
        }
    }

}