package github.afezeria.hymn.common.sql

import github.afezeria.hymn.common.adminConn
import github.afezeria.hymn.common.util.execute
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
const val DEFAULT_ACCOUNT_NAME = "system admin"
const val DEFAULT_ACCOUNT_ID = "911c60ea5d62420794d86eeecfddce7c"
const val DEFAULT_ORG_ID = "b18245e9d690461190172b6cb90c46ac"
const val DEFAULT_ROLE_ID = "301c35c23be449abb5bdf6c80b6878af"

val BASE_ARRAY = arrayOf<Any>(
    DEFAULT_ACCOUNT_ID,
    DEFAULT_ACCOUNT_NAME,
    DEFAULT_ACCOUNT_ID,
    DEFAULT_ACCOUNT_NAME,
    LocalDateTime.now(),
    LocalDateTime.now()
)
val COMMON_INFO =
    arrayOf(DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_NAME)

fun deleteBObject(id: String) {
    adminConn.use {
        val obj = it.execute("select * from hymn.core_b_object where id = ?", id)
        if (obj.isNotEmpty()) {
            it.execute(
                "update hymn.core_b_object_field set active=false where ref_id=? and active=true",
                id
            )
            it.execute("update hymn.core_b_object set active=false where id=?", id)
            it.execute("delete from hymn.core_b_object where id=?", id)
        }
    }
}

val PREDEFINED_OBJECT = listOf("account", "role", "org", "object_type")

fun clearBObject() {
    adminConn.use {
        it.execute(
            "update hymn.core_b_object set active = true where api not in (?,?,?,?)",
            PREDEFINED_OBJECT
        )
        it.execute(
            """
            update hymn.core_b_object_field set active = false 
            where active=true and object_id in 
                (select id from hymn.core_b_object where api not in (?,?,?,?))
        """, PREDEFINED_OBJECT
        )
        it.execute(
            "update hymn.core_b_object set active = false where api not in (?,?,?,?)",
            PREDEFINED_OBJECT
        )
        it.execute("delete from hymn.core_b_object where active = false")
    }
    objSeq = (1..100000).iterator()
}

private var objSeq = (1..100000).iterator()

fun randomUUIDStr(): String = UUID.randomUUID().toString().replace("-", "")

fun createBObject(): Map<String, Any?> {
    adminConn.use {
        val obj = it.execute(
            """
            insert into hymn.core_b_object(name,api,active,can_insert,can_update,can_delete,create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
            values ('测试对象','test_obj${objSeq.nextInt()}',true,true,true,true,?,?,?,?,?,?) returning *;
            """,
            DEFAULT_ACCOUNT_ID,
            DEFAULT_ACCOUNT_NAME,
            DEFAULT_ACCOUNT_ID,
            DEFAULT_ACCOUNT_NAME,
            LocalDateTime.now(),
            LocalDateTime.now()
        )[0]
        val objId = obj["id"] as String
        val type = it.execute(
            """
                insert into hymn.core_b_object_type  (object_id, name, create_by_id, create_by, 
                    modify_by_id, modify_by, create_date, modify_date) 
                values (?, '默认类型',?,?,?,?,now(),now()) returning *;
            """,
            objId, *COMMON_INFO
        )[0]
//        生成默认字段
        it.execute(
            """
            insert into hymn.core_b_object_field  (source_column, object_id, name, api, type, ref_id, 
                ref_delete_policy, standard_type, is_predefined, create_by_id, create_by, modify_by_id, 
                modify_by, create_date, modify_date) 
            values ('owner_id', ?, '所有者','owner_id', 'reference', 'bcf5f00c2e6c494ea2318912a639031a', 
                'null', 'owner_id', true, ?,?,?,?,now(),now());
        """,
            objId, *COMMON_INFO
        )
        it.execute(
            """
            insert into hymn.core_b_object_field  (source_column, object_id, name, api, type, ref_id, 
                ref_delete_policy, standard_type, is_predefined, create_by_id, create_by, modify_by_id, 
                modify_by, create_date, modify_date) 
            values ('create_by_id', ?, '创建人','create_by_id', 'reference', 'bcf5f00c2e6c494ea2318912a639031a', 
                'null', 'create_by_id', true, ?,?,?,?,now(),now());
        """,
            objId, *COMMON_INFO
        )
        it.execute(
            """
            insert into hymn.core_b_object_field  (source_column, object_id, name, api, type, ref_id, 
                ref_delete_policy, standard_type, is_predefined, create_by_id, create_by, modify_by_id, 
                modify_by, create_date, modify_date) 
            values ('modify_by_id', ?, '修改人','modify_by_id', 'reference', 'bcf5f00c2e6c494ea2318912a639031a', 
                'null', 'modify_by_id', true, ?,?,?,?,now(),now());
        """,
            objId, *COMMON_INFO
        )
        it.execute(
            """
            insert into hymn.core_b_object_field  (source_column, object_id, name, api, type, ref_id, 
                ref_delete_policy, standard_type, is_predefined, create_by_id, create_by, modify_by_id, 
                modify_by, create_date, modify_date) 
            values ('type_id', ?, '业务类型','type_id', 'reference', '09da56a7de514895aea5c596820d0ced', 
                'null', 'type_id', true, ?,?,?,?,now(),now());
        """,
            objId, *COMMON_INFO
        )
        it.execute(
            """
            insert into hymn.core_b_object_field  (source_column, object_id, name, api, type, standard_type, 
                is_predefined, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
            values ('create_date', ?, '创建时间', 'create_date', 'datetime', 'create_date', true, 
                ?, ?, ?, ?, now(), now());
        """,
            objId, *COMMON_INFO
        )
        it.execute(
            """
            insert into hymn.core_b_object_field  (source_column, object_id, name, api, type, standard_type, 
                is_predefined, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
            values ('modify_date', ?, '修改时间', 'modify_date', 'datetime', 'modify_date', true, 
                ?, ?, ?, ?, now(), now());
        """,
            objId, *COMMON_INFO
        )
        it.execute(
            """
            insert into hymn.core_b_object_field  (source_column, object_id, name, api, type, standard_type, 
                is_predefined, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
            values ('lock_state', ?, '锁定状态', 'lock_state', 'check_box', 'lock_state', true, 
                ?, ?, ?, ?, now(), now());
        """,
            objId, *COMMON_INFO
        )
        it.execute(
            """
            insert into hymn.core_b_object_field  (source_column, object_id, name, api, type, gen_rule, 
                standard_type, is_predefined, create_by_id, create_by, modify_by_id, modify_by, 
                create_date, modify_date) 
            values ('name', ?, '编号', 'name', 'auto', '{yyyy}{mm}{000}', 'name', true, ?, ?, ?, ?,
                now(),now());
            """,
            objId, *COMMON_INFO
        )
        obj["type_id"] = type["id"]
        return obj
    }
}