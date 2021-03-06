package com.github.afezeria.hymn.common

import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.constant.ClientType
import com.github.afezeria.hymn.common.platform.Session
import com.github.afezeria.hymn.common.sql.core.field.randomFieldNameAndApi
import com.github.afezeria.hymn.common.util.execute
import com.github.afezeria.hymn.common.util.randomUUIDStr
import io.kotest.matchers.shouldBe
import java.sql.Connection
import java.time.LocalDateTime

/**
 * @author afezeria
 */
const val DEFAULT_ACCOUNT_NAME = "system admin"
const val DEFAULT_ACCOUNT_ID = "911c60ea5d62420794d86eeecfddce7c"
const val DEFAULT_ORG_ID = "b18245e9d690461190172b6cb90c46ac"
const val DEFAULT_ROLE_ID = "301c35c23be449abb5bdf6c80b6878af"
const val DEFAULT_ROLE_NAME = "系统管理员"
const val DEFAULT_ORG_NAME = "根组织"

var stopFlag = true
fun stop() {
    while (true) {
        if (!stopFlag) {
            stopFlag = true
            break
        }
        println("wait")
        Thread.sleep(1000)
    }
}


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

val adminSession = Session(
    id = randomUUIDStr(),
    accountType = AccountType.ADMIN,
    accountId = DEFAULT_ACCOUNT_ID,
    accountName = DEFAULT_ACCOUNT_NAME,
    clientType = ClientType.BROWSER,
    roleId = DEFAULT_ROLE_ID,
    roleName = DEFAULT_ROLE_NAME,
    orgId = DEFAULT_ORG_ID,
    orgName = DEFAULT_ORG_NAME,
)
val userSession = Session(
    id = randomUUIDStr(),
    accountType = AccountType.NORMAL,
    accountId = randomUUIDStr(),
    accountName = "normal_user",
    clientType = ClientType.BROWSER,
    roleId = randomUUIDStr(),
    roleName = "普通角色",
    orgId = randomUUIDStr(),
    orgName = "a部门",
)
val anonymousSession = Session(
    id = "",
    accountType = AccountType.ANONYMOUS,
    accountId = "",
    accountName = "",
    clientType = ClientType.BROWSER,
    roleId = "",
    roleName = "",
    orgId = "",
    orgName = ""
)

fun deleteBObject(id: String) {
    adminConn.use {
        val obj = it.execute("select * from hymn.core_biz_object where id = ?", id)
        if (obj.isNotEmpty()) {
            it.execute("update hymn.core_biz_object set active = true where id = ?", id)
//            停用所有引用当前对象的汇总字段
            it.execute(
                """
                    update hymn.core_biz_object_field set active = false where s_id = ? and active=true
                """, id
            )
//            停用当前对象的所有字段
            it.execute(
                "update hymn.core_biz_object_field set active = false where biz_object_id = ?",
                id
            )
//            停用所有引用当前对象的引用字段
            it.execute(
                "update hymn.core_biz_object_field set active=false where ref_id=? and active=true",
                id
            )
            it.execute("update hymn.core_biz_object set active=false where id=?", id)
            it.execute("delete from hymn.core_biz_object where id=?", id)
        }
    }
}

val PREDEFINED_OBJECT = listOf("account", "role", "org", "object_type")

fun clearBObject() {
    adminConn.use {
        it.execute(
            "update hymn.core_biz_object set active = true where api not in (?,?,?,?)",
            PREDEFINED_OBJECT
        )
//        停用汇总字段
        it.execute(
            """
            update hymn.core_biz_object_field set active = false 
            where active=true and type = 'summary' and biz_object_id in 
                (select id from hymn.core_biz_object where api not in (?,?,?,?))
        """, PREDEFINED_OBJECT
        )
//        停用所有字段
        it.execute(
            """
            update hymn.core_biz_object_field set active = false 
            where active=true and biz_object_id in 
                (select id from hymn.core_biz_object where api not in (?,?,?,?))
        """, PREDEFINED_OBJECT
        )
        it.execute(
            "update hymn.core_biz_object set active = false where api not in (?,?,?,?)",
            PREDEFINED_OBJECT
        )
        it.execute("delete from hymn.core_biz_object where active = false")
    }
    objSeq = (1..100000).iterator()
}

var objSeq = (1..100000).iterator()


data class ModuleBizObject(
    val moduleId: String,
    val moduleApi: String,
    val moduleSourceTable: String
)

fun moduleBizObject(fn: ModuleBizObject.(Connection) -> Unit) {
    adminConn.use {
        val seq = objSeq.nextInt()
        val sourceTable = "test_module_object_$seq"
        it.execute(
            """
                create table hymn.$sourceTable(
                    id           text primary key     default replace(public.uuid_generate_v4()::text, '-', ''),
                    name text,
                    atext text,
                    aint bigint,
                    afloat double precision,
                    adatetime timestamptz
                )
            """
        )
        try {
            val data = it.execute(
                """
                insert into hymn.core_biz_object(name,api,type,source_table,create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
                values ('模块对象${seq}','$sourceTable','module','$sourceTable',?,?,?,?,now(),now()) returning *;
                """,
                *COMMON_INFO
            )[0]
            val id = data["id"] as String
            it.execute(
                """
                insert into hymn.core_biz_object_field  (source_column, biz_object_id, name, api, type, gen_rule,     
                    standard_type, predefined, create_by_id, create_by, modify_by_id, modify_by, 
                    create_date, modify_date) 
                values ('name', ?, '编号', 'name', 'auto', '{yyyy}{mm}{000}', 'name', true, ?, ?, ?, ?,
                    now(),now());
                """,
                id, *COMMON_INFO
            )
            try {
                ModuleBizObject(
                    id,
                    data["api"] as String,
                    data["source_table"] as String
                ).fn(it)
            } finally {
                deleteBObject(id)
            }
        } finally {
            it.execute("drop table hymn.$sourceTable cascade")
        }
    }
}

data class RemoteBizObject(val remoteId: String)

fun remoteBizObject(fn: RemoteBizObject.(Connection) -> Unit) {
    adminConn.use {
        val seq = objSeq.nextInt()
        val id = it.execute(
            """
                insert into hymn.core_biz_object(name,api,type,create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
                values ('远程对象${seq}','remote_obj_${seq}','remote',?,?,?,?,now(),now()) returning *;
                """,
            *COMMON_INFO
        )[0]["id"] as String
        try {
            RemoteBizObject(id).fn(it)
        } finally {
            deleteBObject(id)
        }
    }
}

data class CustomBizObject(
    val objId: String,
    val objApi: String,
    val objSourceTable: String,
    val objTypeId: String
) {
    val standardField =
        arrayOf(DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, objTypeId)

    fun Connection.fieldShouldExists(fieldName: Any?, api: String? = null) {
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
            api ?: objApi, fieldName as String
        ).size shouldBe 1
    }

}

fun customBizObject(nameFieldName: String? = null, fn: CustomBizObject.(Connection) -> Unit) {
    adminConn.use {
        val data = createBObject(nameFieldName)
        try {
            CustomBizObject(
                data["id"] as String,
                data["api"] as String,
                data["source_table"] as String,
                data["type_id"] as String
            ).fn(it)
        } finally {
            deleteBObject(data["id"] as String)
        }
    }
}

data class RefBizObject(
    val refId: String,
    val refApi: String,
    val refSourceTable: String,
    val refTypeId: String,
    val masterFieldId: String?,
    val masterFieldApi: String?,
) {
    val standardField =
        arrayOf(DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, DEFAULT_ACCOUNT_ID, refTypeId)

    fun Connection.fieldShouldExists(fieldName: Any?) {
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
            refApi, fieldName as String
        ).size shouldBe 1
    }

}

fun Connection.refBizObject(
    masterId: String = "",
    nameFieldName: String? = null,
    fn: RefBizObject.(Connection) -> Unit
) {
    val data = createBObject(nameFieldName)
    try {
        var masterFieldId: String? = null
        var masterFieldApi: String? = null
        if (masterId.isNotEmpty()) {
            val field = execute(
                """
                    insert into hymn.core_biz_object_field (biz_object_id,name,api,type,ref_id,ref_list_label,ref_delete_policy,
                        create_by_id, create_by, modify_by_id, modify_by,create_date,modify_date) 
                    values (?,${randomFieldNameAndApi("master_slave")},?,'从对象','cascade',?,?,?,?,now(),now()) returning *;
                    """,
                data["id"], masterId, *COMMON_INFO
            )[0]
            masterFieldId = field["id"] as String
            masterFieldApi = field["api"] as String
        }
        RefBizObject(
            data["id"] as String,
            data["api"] as String,
            data["source_table"] as String,
            data["type_id"] as String,
            masterFieldId,
            masterFieldApi,
        ).fn(this)
    } finally {
        deleteBObject(data["id"] as String)
    }
}


fun createBObject(
    nameFieldName: String? = null,
    can_insert: Boolean = true,
    can_update: Boolean = true,
    can_delete: Boolean = true
): Map<String, Any?> {
    adminConn.use {
        val seq = objSeq.nextInt()
        val obj = it.execute(
            """
            insert into hymn.core_biz_object(type,name,api,active,can_insert,can_update,can_delete,create_by_id,create_by,modify_by_id,modify_by,create_date,modify_date)
            values ('custom','测试对象$seq','test_obj${seq}',true,?,?,?,?,?,?,?,?,?) returning *;
            """,
            can_insert, can_update, can_delete,
            DEFAULT_ACCOUNT_ID,
            DEFAULT_ACCOUNT_NAME,
            DEFAULT_ACCOUNT_ID,
            DEFAULT_ACCOUNT_NAME,
            LocalDateTime.now(),
            LocalDateTime.now()
        )[0]
        val objId = obj["id"] as String
        val layout = it.execute(
            """
                insert into hymn.core_biz_object_layout (biz_object_id, name,component_json, 
                    pc_read_layout_json, pc_edit_layout_json, mobile_read_layout_json, 
                    mobile_edit_layout_json, preview_layout_json, 
                    create_by_id, create_by, modify_by_id, modify_by) 
                values (?,'默认布局','','','','','','',?,?,?,?) returning *;
            """,
            objId, *COMMON_INFO
        )[0]
        val layoutId = layout["id"] as String
        val type = it.execute(
            """
                insert into hymn.core_biz_object_type  (biz_object_id, name,default_layout_id,
                    create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
                values (?, '默认类型',?,?,?,?,?,now(),now()) returning *;
            """,
            objId, layoutId, *COMMON_INFO
        )[0]
//        生成默认字段
        if (nameFieldName == null) {
            it.execute(
                """
            insert into hymn.core_biz_object_field  (source_column, biz_object_id, name, api, type, gen_rule, 
                standard_type, predefined, create_by_id, create_by, modify_by_id, modify_by, 
                create_date, modify_date) 
            values ('name', ?, '编号', 'name', 'auto', '{yyyy}{mm}{000}', 'name', true, ?, ?, ?, ?,
                now(),now());
            """,
                objId, *COMMON_INFO
            )
        } else {
            it.execute(
                """
            insert into hymn.core_biz_object_field  (source_column, biz_object_id, name, api, type, 
                max_length,min_length,visible_row,
                standard_type, predefined, create_by_id, create_by, modify_by_id, modify_by, 
                create_date, modify_date) 
            values ('name', ?, ?, 'name', 'text',255,1,1, 'name', true, ?, ?, ?, ?,
                now(),now());
            """,
                objId, nameFieldName, *COMMON_INFO
            )
        }
        it.execute(
            """
            insert into hymn.core_biz_object_field  (source_column, biz_object_id, name, api, type, ref_id, 
                ref_delete_policy, standard_type, predefined, create_by_id, create_by, modify_by_id, 
                modify_by, create_date, modify_date) 
            values ('type_id', ?, '业务类型','type_id', 'reference', '09da56a7de514895aea5c596820d0ced', 
                'set_null', 'type_id', true, ?,?,?,?,now(),now());
        """,
            objId, *COMMON_INFO
        )
        it.execute(
            """
            insert into hymn.core_biz_object_field  (source_column, biz_object_id, name, api, type, standard_type, 
                predefined, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
            values ('lock_state', ?, '锁定状态', 'lock_state', 'check_box', 'lock_state', true, 
                ?, ?, ?, ?, now(), now());
        """,
            objId, *COMMON_INFO
        )
        it.execute(
            """
            insert into hymn.core_biz_object_field  (source_column, biz_object_id, name, api, type, ref_id, 
                ref_delete_policy, standard_type, predefined, create_by_id, create_by, modify_by_id, 
                modify_by, create_date, modify_date) 
            values ('owner_id', ?, '所有者','owner_id', 'reference', 'bcf5f00c2e6c494ea2318912a639031a', 
                'set_null', 'owner_id', true, ?,?,?,?,now(),now());
        """,
            objId, *COMMON_INFO
        )
        it.execute(
            """
            insert into hymn.core_biz_object_field  (source_column, biz_object_id, name, api, type, ref_id, 
                ref_delete_policy, standard_type, predefined, create_by_id, create_by, modify_by_id, 
                modify_by, create_date, modify_date) 
            values ('create_by_id', ?, '创建人','create_by_id', 'reference', 'bcf5f00c2e6c494ea2318912a639031a', 
                'set_null', 'create_by_id', true, ?,?,?,?,now(),now());
        """,
            objId, *COMMON_INFO
        )
        it.execute(
            """
            insert into hymn.core_biz_object_field  (source_column, biz_object_id, name, api, type, ref_id, 
                ref_delete_policy, standard_type, predefined, create_by_id, create_by, modify_by_id, 
                modify_by, create_date, modify_date) 
            values ('modify_by_id', ?, '修改人','modify_by_id', 'reference', 'bcf5f00c2e6c494ea2318912a639031a', 
                'set_null', 'modify_by_id', true, ?,?,?,?,now(),now());
        """,
            objId, *COMMON_INFO
        )
        it.execute(
            """
            insert into hymn.core_biz_object_field  (source_column, biz_object_id, name, api, type, standard_type, 
                predefined, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
            values ('create_date', ?, '创建时间', 'create_date', 'datetime', 'create_date', true, 
                ?, ?, ?, ?, now(), now());
        """,
            objId, *COMMON_INFO
        )
        it.execute(
            """
            insert into hymn.core_biz_object_field  (source_column, biz_object_id, name, api, type, standard_type, 
                predefined, create_by_id, create_by, modify_by_id, modify_by, create_date, modify_date) 
            values ('modify_date', ?, '修改时间', 'modify_date', 'datetime', 'modify_date', true, 
                ?, ?, ?, ?, now(), now());
        """,
            objId, *COMMON_INFO
        )
        obj["type_id"] = type["id"]
        return obj
    }
}

fun Connection.fieldShouldExists(schema: String, tableName: String, fieldName: String) {
    execute(
        """
                select pc.relname,pa.attname
                from pg_class pc
                left join pg_attribute pa on pc.oid=pa.attrelid
                left join pg_namespace pn on pc.relnamespace = pn.oid
                where pn.nspname=?
                and pc.relname=?
                and pa.attname=?
                """,
        schema, tableName, fieldName
    ).size shouldBe 1
}

fun Connection.fieldShouldNotExists(schema: String, tableName: String, fieldName: String) {
    execute(
        """
                select pc.relname,pa.attname
                from pg_class pc
                left join pg_attribute pa on pc.oid=pa.attrelid
                left join pg_namespace pn on pc.relnamespace = pn.oid
                where pn.nspname=?
                and pc.relname=?
                and pa.attname=?
                """,
        schema, tableName, fieldName
    ).size shouldBe 0
}