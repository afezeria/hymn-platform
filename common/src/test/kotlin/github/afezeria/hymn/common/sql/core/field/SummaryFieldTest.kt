package github.afezeria.hymn.common.sql.core.field

import github.afezeria.hymn.common.BaseDbTest
import github.afezeria.hymn.common.COMMON_INFO
import github.afezeria.hymn.common.customBizObject
import github.afezeria.hymn.common.refBizObject
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.common.util.randomUUIDStr
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class SummaryFieldTest : BaseDbTest() {
//    companion object : KLogging() {
//        @JvmStatic
//        @AfterAll
//        fun clear() {
//            clearBObject()
//        }
//
//    }

    @Test
    fun `required field is null`() {
        customBizObject {
            assertSoftly {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,?,'count',0,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        null,
                        randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:09000] 汇总对象/汇总类型/小数位长度不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,?,null,0,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        randomUUIDStr(),
                        randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:09000] 汇总对象/汇总类型/小数位长度不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,?,'count',null,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        randomUUIDStr(),
                        randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:09000] 汇总对象/汇总类型/小数位长度不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,?,'sum',0,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        randomUUIDStr(),
                        null, *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:09100] 汇总字段不能为空"

            }
        }
    }

    @Test
    fun `min_length must be greater than or equal to 0`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,?,'count',-1,?,?,?,?,now(),now()) returning *;
                    """,
                    objId,
                    randomUUIDStr(),
                    randomUUIDStr(), *COMMON_INFO
                )
            }.message shouldContain "[f:inner:09200] 小数位长度必须大于等于0"
        }
    }

    @Test
    fun `summary object does not exists`() {
        customBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,?,'count',1,?,?,?,?,now(),now()) returning *;
                    """,
                    objId,
                    randomUUIDStr(),
                    randomUUIDStr(), *COMMON_INFO
                )
            }.message shouldContain "\\[f:inner:09300\\] 汇总对象 \\[id:[^\\]]+\\] 不存在".toRegex()
        }
    }

    @Test
    fun `summary object is inactive`() {
        customBizObject {
            it.refBizObject {
                it.execute("update hymn.core_biz_object set active = false where id = ?", refId)
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,?,'count',1,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        refId,
                        randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "\\[f:inner:09400\\] 汇总对象 \\[id:.*?\\] 未启用".toRegex()
            }
        }
    }

    @Test
    fun `the summary object is not a child object`() {
        customBizObject {
            val e = shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,?,'count',1,?,?,?,?,now(),now()) returning *;
                    """,
                    objId,
                    objId, randomUUIDStr(), *COMMON_INFO
                )
            }
            e.message shouldContain "[f:inner:09500] 汇总对象必须是当前对象的子对象"
        }
    }

    @Test
    fun `summary field does not exists`() {
        customBizObject {
            it.refBizObject(objId) {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,?,'max',1,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        refId,
                        randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "\\[f:inner:09600\\] 汇总字段 \\[id:[^\\]]+\\] 不存在".toRegex()
            }
        }
    }

    @Test
    fun `wrong summary field type`() {
        customBizObject {
            it.refBizObject(objId) {
                val fieldId = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                    refId, *COMMON_INFO
                )[0]["id"]

                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,?,'max',1,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        refId,
                        fieldId, *COMMON_INFO
                    )
                }.message shouldContain "[f:inner:09700] 最大值、最小值、总和的汇总字段类型必须为：整型/浮点型/金额"
            }
        }
    }

    @Test
    fun `when a object is deleted, the field summarizing the object is also deleted`() {
        customBizObject {
            var summaryFieldId: String? = null
            it.refBizObject(objId) {
                val summaryField = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,'count',0,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, refId, *COMMON_INFO
                )[0]
                summaryFieldId = summaryField["id"] as String
                it.execute(
                    """
                    update hymn.core_biz_object_field set active = false where id = ?
                """, summaryFieldId
                )
            }
//            汇总字段被删除
            summaryFieldId shouldNotBe null
            it.execute(
                """
                    select * from hymn.core_biz_object_field where id=?
                """, summaryFieldId
            ).size shouldBe 0
        }
    }


    @Test
    fun `summary field cannot be active when the target field of summary is inactive or deleted`() {
        customBizObject {
            it.refBizObject(objId) {

                val intField = it.execute(
                    """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("integer")},50000,50,?,?,?,?,now(),now()) returning *;
                    """,
                    refId, *COMMON_INFO
                )[0]
                val intFieldId = intField["id"] as String
                val summaryField = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,?,'sum',0,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, refId, intFieldId, *COMMON_INFO
                )[0]
                val summaryFieldId = summaryField["id"] as String
                it.execute(
                    """
                    update hymn.core_biz_object_field set active = false where id = ?
                """, summaryFieldId
                )
                it.execute(
                    """
                    update hymn.core_biz_object_field set active = false where id = ?
                """, intFieldId
                )
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                        update hymn.core_biz_object_field set active = true where id = ?
                        """,
                        summaryFieldId
                    )
                }.apply {
                    message shouldContain "[f:inner:04100] 汇总目标字段已停用，不能启用当前字段"
                }
//                汇总目标字段被删除时汇总字段一并被删除
                it.execute(
                    """
                    delete from hymn.core_biz_object_field where id = ?
                """, intFieldId
                )
                it.execute(
                    """
                    select * from hymn.core_biz_object_field where id=?
                """, summaryFieldId
                ).size shouldBe 0
            }
        }
    }

    @Test
    fun success() {
        customBizObject {
            it.refBizObject(objId) {
                val field = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("summary")},?,?,'count',0,?,?,?,?,now(),now()) returning *;
                    """,
                    objId, refId, masterFieldId, *COMMON_INFO
                )[0]
                (field["source_column"] as String) shouldStartWith "pl"
                it.execute(
                    """
                    select pc.relname,pa.attname
                    from pg_class pc
                    left join pg_attribute pa on pc.oid=pa.attrelid
                    left join pg_namespace pn on pc.relnamespace = pn.oid
                    where pc.relkind='v'
                    and pn.nspname='hymn_view'
                    and pc.relname=?
                    and pa.attname=?
                    """,
                    objApi, field["api"]
                ).size shouldBe 0
            }
        }

    }
}