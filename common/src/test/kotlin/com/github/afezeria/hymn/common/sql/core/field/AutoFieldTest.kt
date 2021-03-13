package com.github.afezeria.hymn.common.sql.core.field

import com.github.afezeria.hymn.common.BaseDbTest
import com.github.afezeria.hymn.common.COMMON_INFO
import com.github.afezeria.hymn.common.customBizObject
import com.github.afezeria.hymn.common.util.execute
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException
import java.time.LocalDate

/**
 * @author afezeria
 */
class AutoFieldTest : BaseDbTest() {
    @Test
    fun `required field is null`() {
        customBizObject {
            assertSoftly {

                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type,gen_rule, create_by_id, 
                        create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("auto")},null,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:08800] 编号规则不能为空"
            }
        }

    }

    @Test
    fun `there can only be one placeholder in gen_rul`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type,gen_rule, create_by_id, 
                        create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("auto")},'{00}{000}',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )
            }.message shouldContain "[f:inner:08900] 编号规则中有且只能有一个{0}占位符"
        }
    }

    @Test
    fun auto() {
        customBizObject {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type,gen_rule, create_by_id, 
                        create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("auto")},'{yyyy}{mm}{000}',?,?,?,?,now(),now()) returning *;
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
                *standardField, ""
            ).apply {
                size shouldBe 1
                val now = LocalDate.now()
                this[0][field["api"]] as String shouldMatch "^${now.year}${now.monthValue}\\d{3}"
            }
        }
    }
}