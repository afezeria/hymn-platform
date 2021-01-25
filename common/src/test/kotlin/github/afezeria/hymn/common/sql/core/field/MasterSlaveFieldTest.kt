package github.afezeria.hymn.common.sql.core.field

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.common.util.randomUUIDStr
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class MasterSlaveFieldTest : BaseDbTest() {
    @Test
    fun `required field is null`() {
        customBizObject {
            assertSoftly {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("master_slave")},?,null,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:08400] 关联对象/关联对象相关列表标签不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("master_slave")},?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, null, *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:08400] 关联对象/关联对象相关列表标签不能为空"

            }
        }
    }

    @Test
    fun `reference object does not exists`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("master_slave")},?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, randomUUIDStr(), *COMMON_INFO
                )
            }.message shouldContain "\\[f:inner:08500\\] 引用对象 \\[id:[^\\]]+\\] 不存在".toRegex()
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
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("master_slave")},?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        refId, *COMMON_INFO
                    )
                }.message shouldContain "\\[f:inner:08600\\] 引用对象 \\[id:.*?\\] 未启用".toRegex()
            }
        }
    }


    @Test
    fun masteslave() {
        customBizObject {
            val slave = createBObject()
            val slaveId = slave["id"] as String
            val slaveApi = slave["api"] as String
            try {
                val field = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("master_slave")},?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    slaveId, objId, *COMMON_INFO
                )[0]
                (field["source_column"] as String) shouldBe "master001"
                it.fieldShouldExists(field["api"], slave["api"] as String)
                it.execute(
                    """
                        insert into hymn_view.${slaveApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                    DEFAULT_ACCOUNT_ID,
                    DEFAULT_ACCOUNT_ID,
                    DEFAULT_ACCOUNT_ID,
                    slave["type_id"],
                    randomUUIDStr()
                ).size shouldBe 1
            } finally {
                deleteBObject(slaveId)
            }
        }
    }
}