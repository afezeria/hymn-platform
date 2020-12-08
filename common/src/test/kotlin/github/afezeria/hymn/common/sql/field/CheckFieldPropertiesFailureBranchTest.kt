package github.afezeria.hymn.common.sql.field

import github.afezeria.hymn.common.adminConn
import github.afezeria.hymn.common.sql.*
import github.afezeria.hymn.common.util.execute
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.postgresql.util.PSQLException

/**
 * @author afezeria
 */
class CheckFieldPropertiesFailureBranchTest : BaseDbTest() {
    companion object {
        lateinit var objId: String
        lateinit var objSourceTable: String
        lateinit var objApi: String
        lateinit var typeId: String

        lateinit var STANDARD_FIELD: Array<String>

        lateinit var refId: String

        lateinit var childId: String
        lateinit var childMasterFieldId: String

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            val obj = createBObject()
            objId = obj["id"] as String
            objSourceTable = obj["source_table"] as String
            objApi = obj["api"] as String
            typeId = obj["type_id"] as String
            STANDARD_FIELD =
                arrayOf(DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, typeId)
            val ref = createBObject()
            refId = ref["id"] as String
            adminConn.use {
                it.execute("update hymn.core_b_object set active=false where id=?", refId)
            }
            val childObj = createBObject()
            childId = childObj["id"] as String
            adminConn.use {
                val childField = it.execute(
                    """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','masterfield','master_slave',?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    childId, objId, *COMMON_INFO
                )[0]

                childMasterFieldId = childField["id"] as String
            }
        }

        @JvmStatic
        @AfterAll
        fun clear() {
            clearBObject()
        }
    }

    @Test
    fun `predefined field don't set source_column`() {
        adminConn.use {
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
    fun `wrong field type`(){
        adminConn.use {
            val e = shouldThrow<PSQLException> {
                it.execute(
                    """
                    insert into hymn.core_b_object_field (object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, '文本字段', 'tfield', 'abc', 255, 1, 1,  ?, ?, ?, ?, now(), now()) returning *;
                    """,
                    objId, *COMMON_INFO
                )
            }
            e.message shouldContain "无效的字段类型"
        }
    }

    @Test
    fun `required field is null`() {
        adminConn.use {
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
                        randomUUIDStr(),
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
                        randomUUIDStr(),
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
                        randomUUIDStr(),
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
                        objId, *COMMON_INFO
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
                        objId, randomUUIDStr(), *COMMON_INFO
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
                        objId, randomUUIDStr(), *COMMON_INFO
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
                        objId, randomUUIDStr(), *COMMON_INFO
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
                        randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "汇总对象/汇总类型/小数位长度不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'汇总','summaryfield','summary',?,?,null,0,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        randomUUIDStr(),
                        randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "汇总对象/汇总类型/小数位长度不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'汇总','summaryfield','summary',?,?,'count',null,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        randomUUIDStr(),
                        randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "汇总对象/汇总类型/小数位长度不能为空"
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'汇总','summaryfield','summary',?,?,'sum',0,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        randomUUIDStr(),
                        null, *COMMON_INFO
                    )
                }.message shouldContain "汇总字段不能为空"
            }
        }
    }

    @Nested
    inner class TextField {
        @Test
        fun `min_length must be greater than or equal to 0`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, '文本字段', 'tfield', 'text', 255, -1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "最小长度必须大于等于0"
            }
        }

        @Test
        fun `max_length must be less than or equal to 50000`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, '文本字段', 'tfield', 'text', 500000, 1, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "最大长度必须小于等于50000"
            }
        }

        @Test
        fun `max_length must be greater than or equal to min_length`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, '文本字段', 'tfield', 'text', 40, 50, 1, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "最小长度必须小于等于最大长度"
            }
        }

        @Test
        fun `visible_row must be greater than or equal to 0`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?, '文本字段', 'tfield', 'text', 255, 1, 0, ?, ?, ?, ?, now(), now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "可见行数必须大于等于1"
            }
        }
    }

    @Nested
    inner class CheckBoxGroup {
        @Test
        fun `option_number must be greater than or equal to 0`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, default_value, optional_number,  
                        dict_id,create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'复选框组','cbgfield','check_box_group','0',0,?,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "可选个数必须大于0"
            }
        }

        @Test
        fun `dict not exists`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, default_value, optional_number,  
                        dict_id,create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'复选框组','cbgfield','check_box_group','0',1,?,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "字典 \\[id:[^\\]]+\\] 不存在".toRegex()
            }
        }
    }

    @Nested
    inner class Select {
        @Test
        fun `option_number must be greater than or equal to 0`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,'下拉','selectfield','select','0',1,0,?,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, randomUUIDStr(), *COMMON_INFO
                    )

                }.message shouldContain "可选个数必须大于0"
            }
        }

        @Test
        fun `visible_row must be greater than or equal to 0`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,'下拉','selectfield','select','0',0,1,?,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "可见行数必须大于0"
            }
        }

        @Test
        fun `dict not exists`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,'下拉','selectfield','select','0',1,1,?,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "字典 \\[id:[^\\]]+\\] 不存在".toRegex()
            }
        }
    }

    @Nested
    inner class Integer {
        @Test
        fun `min_length must be less than max_length`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type,  max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'整数','intfield','integer',40,50,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "最小值必须小于最大值"
            }
        }
    }

    @Nested
    inner class Float {
        @Test
        fun `min_length must be greater than or equal to 1`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type,  max_length, min_length,  
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'浮点数','floatfield','float',5,0,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "小数位数必须大于等于1"
            }
        }

        @Test
        fun `max_length must be greater than or equal to 1`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type,  max_length, min_length,  
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'浮点数','floatfield','float',0,3,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "整数位数必须大于等于1"
            }
        }

        @Test
        fun `the sum of max_length and min_length must be less than or equal to 18`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type,  max_length, min_length,  
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'浮点数','floatfield','float',10,13,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "总位数必须小于等于18"
            }
        }
    }

    @Nested
    inner class Money {
        @Test
        fun `min_length must be greater than or equal to 1`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field ( object_id, name, api, type, max_length, min_length, 
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'金额','moneyfield','money',5,0,?,?,?,?,now(),now()) returning *;""",
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "小数位数必须大于等于1"
            }
        }

        @Test
        fun `max_length must be greater than or equal to 1`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field ( object_id, name, api, type, max_length, min_length, 
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'金额','moneyfield','money',0,5,?,?,?,?,now(),now()) returning *;""",
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "整数位数必须大于等于1"
            }
        }
    }

    @Nested
    inner class Picture {
        @Test
        fun `min_length must be greater than or equal to 1`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'图片字段','picturefield','picture',1,0,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "图片数量必须大于等于1"
            }
        }

        @Test
        fun `max_length must be greater than or equal to 1`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'图片字段','picturefield','picture',0,1,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "图片大小必须大于1"
            }
        }
    }

    @Nested
    inner class MultipleReference {
        @Test
        fun `reference object does not exists`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_delete_policy,
                        ref_list_label,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','mreffield','mreference',?,'restrict','从对象',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "引用对象 \\[id:[^\\]]+\\] 不存在".toRegex()
            }
        }

        @Test
        fun `reference object is inactive`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_delete_policy,
                        ref_list_label,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','mreffield','mreference',?,'restrict','从对象',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, refId, *COMMON_INFO
                    )
                }.message shouldContain "引用对象未启用"
            }
        }
    }

    @Nested
    inner class Reference {
        @Test
        fun `reference object does not exists`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_delete_policy,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'关联','reffield','reference',?,'null',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "引用对象 \\[id:[^\\]]+\\] 不存在".toRegex()
            }
        }

        @Test
        fun `reference object is inactive`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_delete_policy,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'关联','reffield','reference',?,'null',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, refId, *COMMON_INFO
                    )
                }.message shouldContain "引用对象未启用"
            }
        }
    }

    @Nested
    inner class MasterSlave {
        @Test
        fun `reference object does not exists`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','masterfield','master_slave',?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "引用对象 \\[id:[^\\]]+\\] 不存在".toRegex()
            }
        }

        @Test
        fun `reference object is inactive`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','masterfield','master_slave',?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, refId, *COMMON_INFO
                    )
                }.message shouldContain "引用对象未启用"
            }
        }
    }

    @Nested
    inner class Auto {
        @Test
        fun `there can only be one placeholder in gen_rul`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id, name, api, type,gen_rule, create_by_id, 
                        create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'自动编号字段','autofield','auto','{00}{000}',?,?,?,?,now(),now()) returning *;
                    """,
                        objId, *COMMON_INFO
                    )
                }.message shouldContain "编号规则中有且只能有一个{0}占位符"
            }
        }
    }

    @Nested
    inner class Summary {
        @Test
        fun `min_length must be greater than or equal to 0`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'汇总','summaryfield','summary',?,?,'count',-1,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        randomUUIDStr(),
                        randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "小数位长度必须大于等于0"
            }
        }

        @Test
        fun `summary object does not exists`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'汇总','summaryfield','summary',?,?,'count',1,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        randomUUIDStr(),
                        randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "汇总对象 \\[id:[^\\]]+\\] 不存在".toRegex()
            }
        }

        @Test
        fun `summary object is inactive`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'汇总','summaryfield','summary',?,?,'count',1,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        refId,
                        randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "汇总对象未启用"
            }
        }

        @Test
        fun `the summary object is not a child object`() {
            adminConn.use {
                val e = shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'汇总','summaryfield','summary',?,?,'count',1,?,?,?,?,now(),now()) returning *;
                    """,
                        objId, objId, randomUUIDStr(), *COMMON_INFO
                    )
                }
                e.message shouldContain "汇总对象必须是当前对象的子对象"
            }
        }

        @Test
        fun `summary field does not exists`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'汇总','summaryfield','summary',?,?,'max',1,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        childId,
                        randomUUIDStr(), *COMMON_INFO
                    )
                }.message shouldContain "汇总字段 \\[id:[^\\]]+\\] 不存在".toRegex()
            }
        }

        @Test
        fun `wrong summary field type`() {
            adminConn.use {
                shouldThrow<PSQLException> {
                    it.execute(
                        """
                    insert into hymn.core_b_object_field (object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'汇总','summaryfield','summary',?,?,'max',1,?,?,?,?,now(),now()) returning *;
                    """,
                        objId,
                        childId,
                        childMasterFieldId, *COMMON_INFO
                    )
                }.message shouldContain "最大值、最小值、总和的汇总字段类型必须为：整型/浮点型/金额"
            }
        }
    }
}
