package github.afezeria.hymn.common.sql.field

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class MreferenceFieldTriggerTest : BaseDbTest() {
    companion object {
        lateinit var objId: String
        lateinit var objSourceTable: String
        lateinit var objApi: String
        lateinit var typeId: String

        lateinit var refObj: Map<String, Any?>
        lateinit var refId: String
        lateinit var refApi: String

        lateinit var STANDARD_FIELD: Array<String>

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            adminConn.use {
                val obj = createBObject()
                objId = obj["id"] as String
                objSourceTable = obj["source_table"] as String
                objApi = obj["api"] as String
                typeId = obj["type_id"] as String
                STANDARD_FIELD =
                    arrayOf(DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, typeId)

                refObj = createBObject()
                refId = refObj["id"] as String
                refApi = refObj["api"] as String

            }
        }

        @JvmStatic
        @AfterAll
        fun clear() {
            clearBObject()
        }
    }


    @Test
    fun `ref_id and ref_delete_policy cannot be null`() {
        adminConn.use {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        ref_list_label,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("mreference")},null,null,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )
            }.message shouldContain "[f:inner:04600] 关联对象/关联对象删除策略不能为空"
        }
    }

    @Test
    fun `reference object does not exist`() {
        adminConn.use {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        ref_list_label,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("mreference")},?,'restrict','从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, randomUUIDStr(), *COMMON_INFO
                )
            }.message shouldContain "\\[f:inner:04800\\] 引用对象 \\[id:[^\\]]+\\] 不存在".toRegex()
        }
    }

    @Test
    fun `reference object is inactive`() {
        adminConn.use {
            it.execute("update hymn.core_biz_object set active = false where id = ?", refId)
            try {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        ref_list_label,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("mreference")},?,'restrict','从对象',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, refId, *COMMON_INFO
                    )
                }.message shouldContain "\\[f:inner:04900\\] 引用对象 \\[id:.*?\\] 未启用".toRegex()
            } finally {
                it.execute("update hymn.core_biz_object set active = true where id = ?", refId)
            }
        }
    }

    @Test
    fun `reference object is remote object`() {
        remoteBizObject {
            shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        ref_list_label,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("mreference")},?,'restrict','从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, remoteId, *COMMON_INFO
                )
            }.message shouldContain "[f:inner:05000] 远程对象不能创建多选关联字段或被多选关联字段引用"
        }
    }

    @Test
    fun success() {
        val refObj = createBObject()
        val refId = refObj["id"] as String
        val field: MutableMap<String, Any?>
        val fieldApi: String
        try {
            adminConn.use {
                field = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        ref_list_label,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("mreference")},?,'restrict','从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, refId, *COMMON_INFO
                )[0]
                (field["source_column"] as String) shouldStartWith "mref"
                fieldApi = field["api"] as String
                it.execute(
                    """
                    select *
                    from pg_class pc
                             left join pg_namespace pn on pn.oid = pc.relnamespace
                    where pn.nspname = 'hymn_view'
                      and pc.relkind = 'v'
                    and pc.relname=?
                """, "join_${objApi}_${field["api"]}"
                ).size shouldBe 1
                it.execute(
                    """
                    select *
                    from pg_class pc
                             left join pg_namespace pn on pn.oid = pc.relnamespace
                    where pn.nspname = 'hymn'
                      and pc.relkind = 'r'
                    and pc.relname=?
                """, "core_join_${objApi}_${field["api"]}"
                ).size shouldBe 1
            }
            userConn.use {
                val uuids = (1..5).map { randomUUIDStr() }
                val refDataIds = uuids.joinToString(";")
                val insData = it.execute(
                    """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${fieldApi}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                    *STANDARD_FIELD, refDataIds
                )
                insData.size shouldBe 1
                val dataId = insData[0]["id"] as String
                var data = it.execute(
                    """
                        select * from hymn_view.join_${objApi}_${fieldApi};
                    """
                )
//                多选关联字段的值为以分号分割的5个uuid的字符串时会在中间表生成5条关联数据
                data.size shouldBe 5
                data[0]["s_id"] shouldBe dataId
                data.map { it["t_id"] } shouldContainAll uuids
                it.execute(
                    """
                        update hymn_view.${objApi} set $fieldApi = ? where id = ?
                    """, uuids[0], dataId
                )
//                字段值变为1个uuid的文本后关联表中的4条数据自动被删除
                data = it.execute(
                    """
                        select * from hymn_view.join_${objApi}_${fieldApi};
                    """
                )
                data.size shouldBe 1
                data[0]["s_id"] shouldBe dataId
                data[0]["t_id"] shouldBe uuids[0]
            }
        } finally {
            deleteBObject(refId)
        }
    }


}