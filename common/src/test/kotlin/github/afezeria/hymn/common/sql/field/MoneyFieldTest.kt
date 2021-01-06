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
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException
import java.math.BigDecimal

/**
 * @author afezeria
 */
class MoneyFieldTest : BaseDbTest() {
    @Test
    fun `required field is null`() {
        customBizObject {
            assertSoftly {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field ( biz_object_id, name, api, type, max_length, min_length, 
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("money")},null,3,?,?,?,?,now(),now()) returning *;""",
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:07200] 小数位数/整数位数不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field ( biz_object_id, name, api, type, max_length, min_length, 
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("money")},13,null,?,?,?,?,now(),now()) returning *;""",
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:07200] 小数位数/整数位数不能为空"

            }
        }
    }

    @Test
    fun `min_length must be greater than or equal to 1`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field ( biz_object_id, name, api, type, max_length, min_length, 
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("money")},5,0,?,?,?,?,now(),now()) returning *;""",
                    objId, *COMMON_INFO
                )
            }.message shouldContain "[f:inner:07300] 小数位数必须大于等于1"
        }
    }

    @Test
    fun `max_length must be greater than or equal to 1`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field ( biz_object_id, name, api, type, max_length, min_length, 
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("money")},0,5,?,?,?,?,now(),now()) returning *;""",
                    objId, *COMMON_INFO
                )
            }.message shouldContain "[f:inner:07400] 整数位数必须大于等于1"
        }
    }

    @Test
    fun success() {
        customBizObject {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field ( biz_object_id, name, api, type, max_length, min_length, 
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("money")},13,3,?,?,?,?,now(),now()) returning *;""",
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "decimal"
            it.fieldShouldExists(field["api"])
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *standardField, BigDecimal("1.1")
            ).size shouldBe 1

        }
    }

}