package github.afezeria.hymn.common.sql.core.field

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class ReferenceFieldTest : BaseDbTest() {
    @Test
    fun `required field is null`() {
        customBizObject {
            assertSoftly {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("reference")},?,'null',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, null, *COMMON_INFO
                    )
                    randomFieldNameAndApi("reference")
                }.message shouldContain "[f:inner:08100] 关联对象/关联对象删除策略不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("reference")},?,null,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:08100] 关联对象/关联对象删除策略不能为空"

            }
        }
    }

    @Test
    fun `reference object does not exists`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("reference")},?,'null',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, randomUUIDStr(), *COMMON_INFO
                )
            }.message shouldContain "\\[f:inner:08200\\] 引用对象 \\[id:\\w+\\] 不存在".toRegex()
        }
    }

    @Test
    fun `reference object is inactive`() {
        customBizObject {
            it.refBizObject {
                it.execute("update hymn.core_biz_object set active = false where id = ?", refId)
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("reference")},?,'null',?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        refId, *COMMON_INFO
                    )
                }.message shouldContain "\\[f:inner:08300\\] 引用对象 \\[id:\\w+\\] 未启用".toRegex()
            }
        }
    }

    @Test
    fun success() {
        customBizObject {
            it.refBizObject {
                val field = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("reference")},?,'null',?,?,?,?,now(),now()) returning *;
                    """,
                    refId, objId, *COMMON_INFO
                )[0]
                (field["source_column"] as String) shouldContain "text"
                it.fieldShouldExists(field["api"])
                userConn.use {
                    it.execute(
                        """
                        insert into hymn_view.${refApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                        *standardField, randomUUIDStr()
                    ).size shouldBe 1
                }
            }
        }
    }
}