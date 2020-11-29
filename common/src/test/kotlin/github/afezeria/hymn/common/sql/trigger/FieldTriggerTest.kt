package github.afezeria.hymn.common.sql.trigger

import github.afezeria.hymn.common.conn
import github.afezeria.hymn.common.sql.BaseDbTest
import github.afezeria.hymn.common.sql.COMMON_INFO
import github.afezeria.hymn.common.sql.DEFAULT_ACCOUNT_ID
import github.afezeria.hymn.common.sql.DEFAULT_ACCOUNT_NAME
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.common.util.mapper
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

/**
 * @author afezeria
 */
class FieldTriggerTest : BaseDbTest() {
    companion object {
        lateinit var objId: String
        lateinit var objSourceTable: String

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            conn.use {
                val insert = it.execute("""
insert into hymn.core_b_object(name,api,active,remark,create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
values ('测试对象','test_obj',true,'编号
{yyyy}{mm}{dd}{000}',?,?,?,?,?,?) returning *;""",
                    DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME,
                    LocalDateTime.now(), LocalDateTime.now()
                )[0]
                insert["id"] shouldNotBe null
                objId = insert["id"] as String
                objSourceTable = insert["source_table"] as String
            }
        }
    }

    @Nested
    inner class InsertSuccess {

        @Test
        fun text() {
            conn.use {
                val field = it.execute("insert into hymn.core_b_object_field  ( object_id, name, api, type, max_length, min_length, visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) values (?,'文本字段','tfield','text',255,1,1,?,?,?,?,now(),now()) returning *;", objId, *COMMON_INFO)[0]
                (field["source_column"] as String) shouldStartWith "text"
            }
        }

//        fun `check_box`() {
//            conn.use {
//                val field = it.execute("insert into hymn.core_b_object_field  ( object_id, name, api, type, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) values (?,'预定义字段','a_check_box','check_box',?,?,?,?,now(),now()) returning *;", objId, *COMMON_INFO)[0]
//            }
//        }

        @Test
        fun check_box_group() {
            conn.use {
                val field = it.execute("insert into hymn.core_b_object_field  ( object_id, name, api, type, default_value, optional_number, tmp, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) values (?,'复选框组','cbgfield','check_box_group','0',1,?,?,?,?,?,now(),now()) returning *;", objId,
                    mapper.writeValueAsString(
                        listOf(
                            mapOf("name" to "man", "code" to "0"),
                            mapOf("name" to "woman", "code" to "1"),
                        )
                    ),
                    *COMMON_INFO)[0]
                (field["source_column"] as String) shouldStartWith "text"

            }
        }

        @Test
        fun select() {
            conn.use {
                val field = it.execute("insert into hymn.core_b_object_field  ( object_id, name, api, type, default_value, optional_number, tmp, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) values (?,'复选框组','selectfield','select','0',1,?,?,?,?,?,now(),now()) returning *;", objId,
                    mapper.writeValueAsString(
                        listOf(
                            mapOf("name" to "man", "code" to "0"),
                            mapOf("name" to "woman", "code" to "1"),
                        )
                    ),
                    *COMMON_INFO)[0]
                (field["source_column"] as String) shouldStartWith "text"
            }
        }

        @Test
        fun integer() {
            conn.use {
                val field = it.execute("insert into hymn.core_b_object_field  ( object_id, name, api, type,  max_length, min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) values (?,'整数','intfield','integer',50000,50,?,?,?,?,now(),now()) returning *;", objId, *COMMON_INFO)[0]
                (field["source_column"] as String) shouldStartWith "bigint"
            }
        }

        @Test
        fun float() {
            conn.use {
                val field = it.execute("insert into hymn.core_b_object_field  ( object_id, name, api, type,  max_length, min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) values (?,'浮点数','floatfield','float',13,3,?,?,?,?,now(),now()) returning *;", objId, *COMMON_INFO)[0]
                (field["source_column"] as String) shouldStartWith "double"
            }
        }

        @Test
        fun money() {
            conn.use {
                val field = it.execute("insert into hymn.core_b_object_field  ( object_id, name, api, type,  max_length, min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) values (?,'金额','moneyfield','money',13,3,?,?,?,?,now(),now()) returning *;", objId, *COMMON_INFO)[0]
                (field["source_column"] as String) shouldStartWith "decimal"
            }
        }

        @Test
        fun date() {
            conn.use {
                val field = it.execute("insert into hymn.core_b_object_field  ( object_id, name, api, type,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) values (?,'日期','datefield','date',?,?,?,?,now(),now()) returning *;", objId, *COMMON_INFO)[0]
                (field["source_column"] as String) shouldStartWith "datetime"
            }
        }

        @Test
        fun datetime() {
            conn.use {
                val field = it.execute("insert into hymn.core_b_object_field  ( object_id, name, api, type,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) values (?,'日期时间','datetimefield','datetime',?,?,?,?,now(),now()) returning *;", objId, *COMMON_INFO)[0]
                (field["source_column"] as String) shouldStartWith "datetime"
            }
        }

        @Test
        fun auto() {
            conn.use {
                val field = it.execute("insert into hymn.core_b_object_field  ( object_id, name, api, type,gen_rule, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) values (?,'自动编号字段','autofield','auto','{yyyy}{mm}{000}',?,?,?,?,now(),now()) returning *;", objId, *COMMON_INFO)[0]
                (field["source_column"] as String) shouldStartWith "text"
            }
        }

        @Test
        fun picture() {
            conn.use {
                val field = it.execute("insert into hymn.core_b_object_field  ( object_id, name, api, type, max_length, min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) values (?,'图片字段','picturefield','picture',1024,1,?,?,?,?,now(),now()) returning *;", objId, *COMMON_INFO)[0]
                (field["source_column"] as String) shouldStartWith "text"
            }
        }
//缺少主详、关联关系和汇总字段测试
    }

}