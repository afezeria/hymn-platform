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

/**
 * @author afezeria
 */
class PictureFieldTest : BaseDbTest() {
    @Test
    fun `required field is null`() {
        customBizObject {
            assertSoftly {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("picture")},null,1,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:07500] 图片数量/图片大小不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("picture")},1024,null,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:07500] 图片数量/图片大小不能为空"

            }
        }
    }

    @Test
    fun `min_length must be greater than or equal to 1`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("picture")},1,0,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )
            }.message shouldContain "[f:inner:07600] 图片数量必须大于等于1"
        }
    }

    @Test
    fun `max_length must be greater than or equal to 1`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("picture")},0,1,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )
            }.message shouldContain "[f:inner:07700] 图片大小必须大于1"
        }
    }

    @Test
    fun success() {
        customBizObject {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("picture")},1024,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "picture"
            it.fieldShouldExists(field["api"])
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *standardField, ""
            ).size shouldBe 1
        }
    }

}