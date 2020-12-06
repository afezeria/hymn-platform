package github.afezeria.hymn.common.sql.trigger

import github.afezeria.hymn.common.conn
import github.afezeria.hymn.common.sql.*
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException
import java.util.*

/**
 * @author afezeria
 */
class FieldFailureBranchTest : BaseDbTest() {
    companion object {
        lateinit var objId: String
        lateinit var objSourceTable: String
        lateinit var objApi: String
        lateinit var typeId: String

        lateinit var STANDARD_FIELD: Array<String>

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            conn.use {
                val obj = createBObject()
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
    fun `predefined field don't set source_column`() {
        conn.use {
            val e = shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_b_object_field (source_column,object_id, name, api, type, max_length, min_length, 
                        visible_row, is_predefined,create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (null, ?, '文本字段', 'tfield', 'text', 255, 1, 1, true, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                    objId,
                    *COMMON_INFO
                )
            }
            e.message shouldContain "预定义字段必须指定 source_column"
        }
    }

    @Test
    fun `required field is null`() {
        conn.use {
            assertSoftly {
//                text
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, '文本字段', 'tfield', 'text', null, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "最小长度、最大长度和显示行数都不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, '文本字段', 'tfield', 'text', 255, null, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "最小长度、最大长度和显示行数都不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, '文本字段', 'tfield', 'text', 255, 1, null, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "最小长度、最大长度和显示行数都不能为空"

//                check_box_group
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, default_value, optional_number,  
                        dict_id,create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'复选框组','cbgfield','check_box_group','0',null,?,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        UUID.randomUUID(),
                        *COMMON_INFO
                    )
                }.message shouldContain "可选个数/字典项不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, default_value, optional_number,  
                        dict_id,create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'复选框组','cbgfield','check_box_group','0',1,?,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        null,
                        *COMMON_INFO
                    )
                }.message shouldContain "可选个数/字典项不能为空"

//                select
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,'下拉','selectfield','select','0',1,null,?,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        UUID.randomUUID(),
                        *COMMON_INFO
                    )
                }.message shouldContain "可选个数/可见行数/字典项不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,'下拉','selectfield','select','0',null,1,?,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        UUID.randomUUID(),
                        *COMMON_INFO
                    )
                }.message shouldContain "可选个数/可见行数/字典项不能为空"

                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,'下拉','selectfield','select','0',1,1,?,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        null,
                        *COMMON_INFO
                    )
                }.message shouldContain "可选个数/可见行数/字典项不能为空"

//                integer
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type,  max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'整数','intfield','integer',null,50,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "最小值/最大值不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type,  max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'整数','intfield','integer',50000,null,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "最小值/最大值不能为空"

//                float
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type,  max_length, min_length,  
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'浮点数','floatfield','float',null,3,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "小数位数/整数位数不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type,  max_length, min_length,  
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'浮点数','floatfield','float',13,null,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "小数位数/整数位数不能为空"

//                money
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field ( object_id, name, api, type, max_length, min_length, 
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'金额','moneyfield','money',null,3,?,?,?,?,now(),now()) returning *;""",
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "小数位数/整数位数不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field ( object_id, name, api, type, max_length, min_length, 
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'金额','moneyfield','money',13,null,?,?,?,?,now(),now()) returning *;""",
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "小数位数/整数位数不能为空"

//                picture
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'图片字段','picturefield','picture',null,1,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "图片数量/图片大小不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'图片字段','picturefield','picture',1024,null,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "图片数量/图片大小不能为空"

//                multiple_reference
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_delete_policy,
                        ref_list_label,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','mreffield','mreference',?,'restrict','从对象',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, null, *COMMON_INFO
                    )
                }.message shouldContain "关联对象/关联对象删除策略不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_delete_policy,
                        ref_list_label,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','mreffield','mreference',?,null,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, UUID.randomUUID(), *COMMON_INFO
                    )
                }.message shouldContain "关联对象/关联对象删除策略不能为空"

//                reference
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_delete_policy,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'关联','reffield','reference',?,'null',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, null, *COMMON_INFO
                    )
                }.message shouldContain "关联对象/关联对象删除策略不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_delete_policy,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'关联','reffield','reference',?,null,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, UUID.randomUUID(), *COMMON_INFO
                    )
                }.message shouldContain "关联对象/关联对象删除策略不能为空"

//                master_slave
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','masterfield','master_slave',?,null,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, UUID.randomUUID(), *COMMON_INFO
                    )
                }.message shouldContain "关联对象/关联对象相关列表标签不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','masterfield','master_slave',?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, null, *COMMON_INFO
                    )
                }.message shouldContain "关联对象/关联对象相关列表标签不能为空"

//                auto
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id, name, api, type,gen_rule, create_by_id, 
                        create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'自动编号字段','autofield','auto',null,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        *COMMON_INFO
                    )
                }.message shouldContain "编号规则不能为空"

//                summary
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'汇总','summaryfield','summary',?,?,'count',0,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        null,
                        UUID.randomUUID(), *COMMON_INFO
                    )
                }.message shouldContain "汇总对象/汇总类型不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'汇总','summaryfield','summary',?,?,null,0,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        UUID.randomUUID(),
                        UUID.randomUUID(), *COMMON_INFO
                    )
                }.message shouldContain "汇总对象/汇总类型不能为空"
            }
        }
    }
}