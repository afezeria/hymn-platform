package com.github.afezeria.hymn.common.sql.core.field

import com.github.afezeria.hymn.common.BaseDbTest
import com.github.afezeria.hymn.common.COMMON_INFO
import com.github.afezeria.hymn.common.customBizObject
import com.github.afezeria.hymn.common.util.execute
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
class FloatFieldTest : BaseDbTest() {

    @Test
    fun `required field is null`() {
        customBizObject {
            assertSoftly {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  max_length, min_length,  
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("float")},null,3,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:06800] 小数位数/整数位数不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  max_length, min_length,  
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("float")},13,null,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:06800] 小数位数/整数位数不能为空"

            }
        }
    }

    @Test
    fun `min_length must be greater than or equal to 1`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  max_length, min_length,  
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("float")},5,0,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )
            }.message shouldContain "[f:inner:06900] 小数位数必须大于等于1"
        }
    }

    @Test
    fun `max_length must be greater than or equal to 1`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  max_length, min_length,  
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("float")},0,3,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )
            }.message shouldContain "[f:inner:07000] 整数位数必须大于等于1"
        }
    }

    @Test
    fun `the sum of max_length and min_length must be less than or equal to 18`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  max_length, min_length,  
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("float")},10,13,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )
            }.message shouldContain "[f:inner:07100] 总位数必须小于等于18"
        }
    }

    @Test
    fun success() {
        customBizObject {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  max_length, min_length,  
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("float")},13,3,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "double"
            it.fieldShouldExists(field["api"])
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *standardField, 1.1
            ).size shouldBe 1

        }
    }

}