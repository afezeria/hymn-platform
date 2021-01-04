package github.afezeria.hymn.common.sql.field

import github.afezeria.hymn.common.*
import github.afezeria.hymn.common.util.execute
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldStartWith
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.time.LocalDate
import java.time.LocalDateTime

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
            adminConn.use {
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
            clearBObject()
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
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type, max_length, min_length, 
                        visible_row, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,${randomFieldNameAndApi("text")},255,1,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "text"
            it.fieldShouldExists((field["api"] as String).replace("__cf",""))
            it.execute(
                """
                        insert into hymn_view.$objApi (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, "abc"
            ).size shouldBe 1
        }
    }

    @Test
    fun `check_box`() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'单选字段','cbfield','check_box',?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "bool"
            it.fieldShouldExists("cbfield")
            it.execute(
                """
                        insert into hymn_view.$objApi (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, false
            ).size shouldBe 1
        }
    }

    @Test
    fun check_box_group() {
        adminConn.use {
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
                insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, default_value, optional_number,  
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
                        insert into hymn_view.$objApi (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, "0"
            ).size shouldBe 1

        }
    }

    @Test
    fun select() {
        adminConn.use {
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
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, default_value, 
                        visible_row,optional_number, dict_id, create_by_id, create_by, modify_by_id, 
                        modify_by, create_date, modify_date) 
                    values (?,'下拉','selectfield','select','0',1,1,?,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                dict["id"],
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "text"
            it.fieldShouldExists("selectfield")
            it.execute(
                """
                        insert into hymn_view.$objApi (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, "0"
            ).size shouldBe 1

        }
    }

    @Test
    fun integer() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  max_length, 
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
                        insert into hymn_view.$objApi (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, 1
            ).size shouldBe 1

        }
    }

    @Test
    fun float() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  max_length, min_length,  
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
                        insert into hymn_view.$objApi (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, 1.1
            ).size shouldBe 1

        }
    }

    @Test
    fun money() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field ( biz_object_id, name, api, type, max_length, min_length, 
                        create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'金额','moneyfield','money',13,3,?,?,?,?,now(),now()) returning *;""",
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "decimal"
            it.fieldShouldExists("moneyfield")
            it.execute(
                """
                        insert into hymn_view.$objApi (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, 1.1
            ).size shouldBe 1

        }
    }

    @Test
    fun date() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  create_by_id, 
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
                        insert into hymn_view.$objApi (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, LocalDate.now()
            ).size shouldBe 1

        }
    }

    @Test
    fun datetime() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type,  create_by_id, 
                        create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'日期时间','datetimefield','datetime',?,?,?,?,now(),now()) returning *;""",
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "datetime"
            it.fieldShouldExists("datetimefield")
            it.execute(
                """
                        insert into hymn_view.$objApi (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, LocalDateTime.now()
            ).size shouldBe 1

        }
    }

    @Test
    fun auto() {
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id, name, api, type,gen_rule, create_by_id, 
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
                        insert into hymn_view.$objApi (create_date,modify_date,owner_id,create_by_id,
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
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field  ( biz_object_id, name, api, type, max_length, 
                        min_length,  create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                    values (?,'图片字段','picturefield','picture',1024,1,?,?,?,?,now(),now()) returning *;
                    """,
                objId,
                *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldStartWith "picture"
            it.fieldShouldExists("picturefield")
            it.execute(
                """
                        insert into hymn_view.$objApi (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                *STANDARD_FIELD, ""
            ).size shouldBe 1

        }
    }


    @Test
    fun multipleReference() {

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
                    values (?,'多选关联对象','mreffield8','mreference',?,'restrict','从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, refId, *COMMON_INFO
                )[0]
                (field!!["source_column"] as String) shouldStartWith "mref"
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
                        insert into hymn_view.$objApi (create_date,modify_date,owner_id,create_by_id,
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
                        update hymn_view.$objApi set $fieldApi = ? where id = ?
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

    @Test
    fun reference() {
        val refObj = createBObject()
        val refId = refObj["id"] as String
        val fieldApi: String
        try {
            adminConn.use {
                val field = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_delete_policy,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'关联','reffield','reference',?,'null',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, refObj["id"], *COMMON_INFO
                )[0]
                fieldApi = field["api"] as String
                (field["source_column"] as String) shouldContain "text"
                it.fieldShouldExists("reffield")
            }
            userConn.use {
                it.execute(
                    """
                        insert into hymn_view.$objApi (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${fieldApi}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                    *STANDARD_FIELD, randomUUIDStr()
                ).size shouldBe 1
            }
        } finally {
            deleteBObject(refId)
        }
    }

    @Test
    @Order(10)
    fun masteslave() {
        val master = createBObject()
        val masterId = master["id"] as String
        try{
            adminConn.use {
                val field = it.execute(
                    """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','masterfield','master_slave',?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                    objId, masterId, *COMMON_INFO
                )[0]
                (field["source_column"] as String) shouldBe "master001"
                it.fieldShouldExists("masterfield")
                masterObjId = masterId
                masterFieldId = field["id"] as String
                it.execute(
                    """
                        insert into hymn_view.$objApi (create_date,modify_date,owner_id,create_by_id,
                            modify_by_id,type_id,${field["api"]}) 
                        values (now(), now(), ?, ?, ?, ?, ?) returning *;""",
                    *STANDARD_FIELD, randomUUIDStr()
                ).size shouldBe 1
            }
        }finally {
            deleteBObject(masterId)
        }
    }

    @Test
    @Order(20)
    fun summary() {
        val master = createBObject()
        val masterId = master["id"] as String
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'主对象','masterfield','master_slave',?,'从对象',?,?,?,?,now(),now()) returning *;
                    """,
                objId, masterId, *COMMON_INFO
            )[0]
            (field["source_column"] as String) shouldBe "master001"
            it.fieldShouldExists("masterfield")
            masterObjId = masterId
            masterFieldId = field["id"] as String
        }
        adminConn.use {
            val field = it.execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,s_id , s_field_id , 
                    s_type , min_length ,create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,'汇总','summaryfield','summary',?,?,'count',0,?,?,?,?,now(),now()) returning *;
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