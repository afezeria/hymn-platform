package github.afezeria.hymn.common.sql.trigger

import github.afezeria.hymn.common.conn
import github.afezeria.hymn.common.sql.*
import github.afezeria.hymn.common.util.execute
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
class FieldInsertSuccessTest : BaseDbTest() {
    companion object {
        lateinit var objId: String
        lateinit var objSourceTable: String
        lateinit var objApi: String
        lateinit var typeId: String

        lateinit var masterObjId: String
        lateinit var masterFieldId: String

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

        fun Connection.fieldShouldExists(fieldName: String) {
            execute(
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
                objApi, fieldName + "__cf"
            ).size shouldBe 1
        }
    }

    @Test
    fun text() {
        conn.use {
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field (object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'文本字段','tfield','text',255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "text"
            it.fieldShouldExists("tfield")
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, "abc"
            ).size shouldBe 1
        }
    }

    @Test
    fun `check_box`() {
        conn.use {
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'单选字段','cbfield','check_box',?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "bool"
            it.fieldShouldExists("cbfield")
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, false
            ).size shouldBe 1
        }
    }

    @Test
    fun check_box_group() {
        conn.use {
            val dict = it.execute(
                """
                        insert into hymn.core_dict  ( name, api,  create_by_id, create_by, modify_by_id, 
                            modify_by, create_date, modify_date) 
                        values ('测试字典','test_dict',?,?,?,?,now(),now()) returning *;
                        """,
                *COMMON_INFO
            )[0]
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, default_value, optional_number,  
                        dict_id,create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'复选框组','cbgfield','check_box_group','0',1,?,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                dict["id"],
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "text"

            it.fieldShouldExists("cbgfield")
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, "0"
            ).size shouldBe 1

        }
    }

    @Test
    fun select() {
        conn.use {
            val dict = it.execute(
                """
                    insert into hymn.core_dict  ( name, api,  create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values ('测试字典','test_dict5',?,?,?,?,now(),now()) returning *;
                    """,
                *COMMON_INFO
            )[0]
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,'复选框组','selectfield','select','0',1,1,?,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                dict["id"],
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "text"
            it.fieldShouldExists("selectfield")
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, "0"
            ).size shouldBe 1

        }
    }

    @Test
    fun integer() {
        conn.use {
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type,  max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'整数','intfield','integer',50000,50,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "bigint"
            it.fieldShouldExists("intfield")
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, 1
            ).size shouldBe 1

        }
    }

    @Test
    fun float() {
        conn.use {
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type,  max_length, min_length,  
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'浮点数','floatfield','float',13,3,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "double"
            it.fieldShouldExists("floatfield")
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, 1.1
            ).size shouldBe 1

        }
    }

    @Test
    fun money() {
        conn.use {
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field ( object_id, name, api, type, max_length, min_length, 
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'金额','moneyfield','money',13,3,?,?,?,?,now(),now()) returning *;""",
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "decimal"
            it.fieldShouldExists("moneyfield")
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, 1.1
            ).size shouldBe 1

        }
    }

    @Test
    fun date() {
        conn.use {
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type,  create_by_id, 
                        create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'日期','datefield','date',?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "datetime"
            it.fieldShouldExists("datefield")
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, LocalDate.now()
            ).size shouldBe 1

        }
    }

    @Test
    fun datetime() {
        conn.use {
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type,  create_by_id, 
                        create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'日期时间','datetimefield','datetime',?,?,?,?,now(),now()) returning *;""",
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "datetime"
            it.fieldShouldExists("datetimefield")
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, LocalDateTime.now()
            ).size shouldBe 1

        }
    }

    @Test
    fun auto() {
        conn.use {
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field (object_id, name, api, type,gen_rule, create_by_id, 
                        create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'自动编号字段','autofield','auto','{yyyy}{mm}{000}',?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "text"
            it.fieldShouldExists("autofield")
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, ""
            ).apply {
                size shouldBe 1
                val now = LocalDate.now()
                this[0][field["api"]] as String shouldMatch "^${now.year}${now.monthValue}\\d{3}"
            }
        }
    }

    @Test
    fun picture() {
        conn.use {
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field  ( object_id, name, api, type, max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'图片字段','picturefield','picture',1024,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "text"
            it.fieldShouldExists("picturefield")
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, ""
            ).size shouldBe 1

        }
    }


    @Test
    fun multipleReference() {

        conn.use {
            val masterObj = it.execute(
                """
                    insert into hymn.core_b_object(name,api,active,create_by_id,create_by,modify_by_id,
                        modify_by,create_date,modify_date)
                    values ('测试对象3','test_obj3',true,?,?,?,?,?,?) returning *;
                    """,
                DEFAULT_ACCOUNT_ID,
                DEFAULT_ACCOUNT_NAME,
                DEFAULT_ACCOUNT_ID,
                DEFAULT_ACCOUNT_NAME,
                LocalDateTime.now(),
                LocalDateTime.now()
            )[0]

            val field = it.execute(
                """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_delete_policy,
                        ref_list_label,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','mreffield','mreference',?,'restrict','从对象',?,?,?,?,now(),now()) returning *;
                    """,
                objId, masterObj["id"], *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "pl"
            it.execute(
                """
                    select *
                    from pg_class pc
                             left join pg_namespace pn on pn.oid = pc.relnamespace
                    where pn.nspname = 'hymn_view'
                      and pc.relkind = 'v'
                    and pc.relname=?
                """, "dt_join_${objApi}_${field["api"]}"
            ).size shouldBe 1
            it.execute(
                """
                    select *
                    from pg_class pc
                             left join pg_namespace pn on pn.oid = pc.relnamespace
                    where pn.nspname = 'hymn'
                      and pc.relkind = 'r'
                    and pc.relname=?
                """, "core_dt_join_${objApi}_${field["api"]}"
            ).size shouldBe 1
            it.execute(
                """
                    insert into hymn_view.dt_join_${objApi}_${field["api"]} (s_id,t_id) values (?,?) returning *;
                """, UUID.randomUUID(), UUID.randomUUID()
            ).size shouldBe 1

        }
    }

    @Test
    @Order(10)
    fun masteslave() {
        conn.use {
            val masterObj = it.execute(
                """
                    insert into hymn.core_b_object(name,api,active,create_by_id,create_by,modify_by_id,
                        modify_by,create_date,modify_date)
                    values ('测试对象2','test_obj2',true,?,?,?,?,?,?) returning *;
                    """,
                DEFAULT_ACCOUNT_ID,
                DEFAULT_ACCOUNT_NAME,
                DEFAULT_ACCOUNT_ID,
                DEFAULT_ACCOUNT_NAME,
                LocalDateTime.now(),
                LocalDateTime.now()
            )[0]

            val field = it.execute(
                """
                    insert into hymn.core_b_object_field (object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','masterfield','master_slave',?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                objId, masterObj["id"], *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldBe "master001"
            it.fieldShouldExists("masterfield")
            masterObjId = masterObj["id"] as String
            masterFieldId = field["id"] as String
            it.execute(
                """
                        insert into hymn_view.${objApi} (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, UUID.randomUUID()
            ).size shouldBe 1

        }
    }

    @Test
    @Order(20)
    fun summary() {
        conn.use {
            val field = it.execute(
                """
                    insert into hymn.core_b_object_field (object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','summaryfield','summary',?,?,'count',0,?,?,?,?,now(),now()) returning *;
                    """,
                masterObjId, objId, masterFieldId, *COMMON_INFO
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