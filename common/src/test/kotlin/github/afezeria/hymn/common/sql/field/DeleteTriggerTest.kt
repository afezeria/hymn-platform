package github.afezeria.hymn.common.sql.field

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.common.util.toFormatJson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import mu.KLogging
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class DeleteTriggerTest : BaseDbTest() {
    companion object : KLogging() {
        lateinit var objId: String
        lateinit var objSourceTable: String
        lateinit var objApi: String
        lateinit var typeId: String
        lateinit var obj: Map<String, Any?>

        lateinit var STANDARD_FIELD: Array<String>

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            adminConn.use {
                obj = createBObject()
                objId = obj["id"] as String
                objSourceTable = obj["source_table"] as String
                objApi = obj["api"] as String
                typeId = obj["type_id"] as String
                STANDARD_FIELD =
                    arrayOf(DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, typeId)
            }
        }

        @JvmStatic
        @AfterAll
        fun clear() {
            deleteBObject(objId)
        }
    }

    @Test
    fun `cannot delete active field`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'文本字段','tfield','text',255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            val e = shouldThrow<PSQLException> {
                it.execute("delete from hymn.core_biz_object_field where id = ?", field["id"])
            }
            e.message shouldContain "字段 ${field["api"]} 未停用，无法删除"
        }
    }

    @Test
    fun `release column resources and clear data`() {
        val field: MutableMap<String, Any?>
        adminConn.use {
            field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'文本字段','tfield2','text',255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId, *COMMON_INFO
            )[0]
            it.execute(
                "select * from hymn.core_column_field_mapping where table_name=? and field_api=?",
                objSourceTable, field["api"]
            ).size shouldBe 1
        }
        userConn.use {
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?,'test') returning *;""",
                DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, typeId
            )[0]
        }
        adminConn.use {
            it.execute(
                "select * from hymn_view.${objApi} where ${field["api"]} is not null"
            ).size shouldBe 1
            it.execute(
                "select * from hymn.${objSourceTable} where ${field["source_column"]} is not null"
            ).size shouldBe 1
            it.execute("update hymn.core_biz_object_field set active=false where id=?", field["id"])
            it.execute("delete from hymn.core_biz_object_field where id = ?", field["id"])
            it.execute(
                "select * from hymn.core_column_field_mapping where table_name=? and field_api=?",
                objSourceTable, field["api"]
            ).size shouldBe 0
            it.execute(
                "select * from hymn.${objSourceTable} where ${field["source_column"]} is not null"
            ).size shouldBe 0
        }
    }

    @Test
    fun `delete the join_table when the deleted field type is mreference`() {
        val refObj = createBObject()
        val refId = refObj["id"] as String
        try {
            adminConn.use {
                val field = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        ref_list_label,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'多选关联对象','mreffield7','mreference',?,'restrict','从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, refId, *COMMON_INFO
                )[0]
//                存在中间表
                it.execute(
                    """
                        select * from pg_class pc left join pg_namespace pn on pc.relnamespace = pn.oid
                        where pn.nspname='hymn'
                        and pc.relkind='r'
                        and pc.relname='core_${field["join_view_name"]}'
                    """
                ).size shouldBe 1
//                存在中间表视图
                it.execute(
                    """
                        select * from pg_class pc left join pg_namespace pn on pc.relnamespace = pn.oid
                        where pn.nspname='hymn_view'
                        and pc.relkind='v'
                        and pc.relname='${field["join_view_name"]}'
                    """
                ).size shouldBe 1
//                存在多选关联字段触发器
                it.execute(
                    """
                    select *
                    from pg_trigger pt
                             left join pg_class pc on pt.tgrelid = pc.oid
                             left join pg_namespace pn on pn.oid = pc.relnamespace
                    where pc.relname = '${objSourceTable}'
                      and pn.nspname = 'hymn'
                      and pt.tgname = 'a20_' || '${objSourceTable}' || '_mref_trigger';
                    """
                ).size shouldBe 1
//                存在函数
                it.execute(
                    """
                        select * 
                        from pg_proc pp
                        left join pg_namespace pn on pn.oid = pp.pronamespace
                        where pn.nspname = 'hymn'
                        and pp.proname = '${objSourceTable}' || '_mref_trigger_function'
                    """
                ).size shouldBe 1
                logger.info("==================================================update")
                it.execute(
                    "update hymn.core_biz_object_field set active=false where id=?",
                    field["id"]
                )
                logger.info("==================================================delete")
                val deleted = it.execute(
                    "delete from hymn.core_biz_object_field where id=? returning *",
                    field["id"]
                )
                logger.info(deleted[0].toFormatJson())
                logger.info("==================================================")
//                中间表已删除
                it.execute(
                    """
                        select * from pg_class pc left join pg_namespace pn on pc.relnamespace = pn.oid
                        where pn.nspname='hymn'
                        and pc.relkind='r'
                        and pc.relname='core_${field["join_view_name"]}'
                    """
                ).size shouldBe 0
//                中间视图已删除
                it.execute(
                    """
                        select * from pg_class pc left join pg_namespace pn on pc.relnamespace = pn.oid
                        where pn.nspname='hymn_view'
                        and pc.relkind='v'
                        and pc.relname='${field["join_view_name"]}'
                    """
                ).size shouldBe 0
//                多选关联触发器已删除
                it.execute(
                    """
                    select *
                    from pg_trigger pt
                             left join pg_class pc on pt.tgrelid = pc.oid
                             left join pg_namespace pn on pn.oid = pc.relnamespace
                    where pc.relname = '${objSourceTable}'
                      and pn.nspname = 'hymn'
                      and pt.tgname = 'a20_' || '${objSourceTable}' || '_mref_trigger';
                    """
                ).size shouldBe 0
//                函数已删除
                it.execute(
                    """
                        select * 
                        from pg_proc pp
                        left join pg_namespace pn on pn.oid = pp.pronamespace
                        where pn.nspname = 'hymn'
                        and pp.proname = '${objSourceTable}' || '_mref_trigger_function'
                    """
                ).size shouldBe 0


            }
        } finally {
            deleteBObject(refId)
        }
    }

}