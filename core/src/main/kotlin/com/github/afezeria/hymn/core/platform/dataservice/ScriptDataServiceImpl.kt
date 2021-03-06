package com.github.afezeria.hymn.core.platform.dataservice

import com.github.afezeria.hymn.common.constant.TriggerEvent.*
import com.github.afezeria.hymn.common.exception.DataNotFoundException
import com.github.afezeria.hymn.common.exception.InnerException
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.common.platform.dataservice.*
import com.github.afezeria.hymn.common.util.execute
import com.github.afezeria.hymn.core.module.service.*
import com.github.afezeria.hymn.core.platform.script.ScriptService
import mu.KLoggable
import mu.KLogger
import org.ktorm.database.Database
import java.util.*
import kotlin.collections.HashMap

/**
 * @author afezeria
 */
class ScriptDataServiceImpl(
    private val accountService: AccountService,
    private val bizObjectService: BizObjectService,
    private val typeService: BizObjectTypeService,
    private val fieldService: BizObjectFieldService,
    private val objectPermService: BizObjectPermService,
    private val typePermService: BizObjectTypePermService,
    private val fieldPermService: BizObjectFieldPermService,
    private val databaseService: DatabaseService,
    private val scriptService: ScriptService,
    memorize: Boolean = true,
) : ScriptDataService, ScriptDataServiceForQuery,
    ScriptDataServiceForInsert, ScriptDataServiceForUpdate, ScriptDataServiceForDelete,
    ScriptDataServiceForShare, KLoggable {

    inner class EmptyMap<K, V> : AbstractMap<K, V>() {
        override val entries: MutableSet<MutableMap.MutableEntry<K, V>> = mutableSetOf()

        override fun put(key: K, value: V): V? = null
    }

    private val cache: MutableMap<String, Any?> = if (memorize) HashMap() else EmptyMap()

    private val fieldPermCache: MutableMap<String, List<FieldPerm>> =
        if (memorize) HashMap() else EmptyMap()

    private val tmpMap: MutableMap<String, Any?> = HashMap()

    private val wrapper = DataServiceWrapper(this)

    private val stack = Stack<String>()

    override val logger: KLogger = logger()

    override val database: Database
        get() = databaseService.user()

    override fun execute(
        beforeExecutingSql: (
            oldData: Map<String, Any?>?,
            newData: MutableMap<String, Any?>?,
        ) -> Pair<String, Collection<Any?>>,
        afterExecutingSql: (MutableMap<String, Any?>?) -> Pair<MutableMap<String, Any?>, MutableMap<String, Any?>?>,
        type: WriteType,
        objectApiName: String,
        oldData: Map<String, Any?>?,
        newData: NewRecordMap?,
        withTrigger: Boolean,
    ): MutableMap<String, Any?> {
        val old: Map<String, Any?>? = oldData?.let { Collections.unmodifiableMap(it) }
        var new: MutableMap<String, Any?>? = newData

        val bizObjectId = requireNotNull(getObjectByApi(objectApiName)).id
        var callback: ((String, () -> Unit) -> Unit)? = null
        if (withTrigger) {
            val event = when (type) {
                WriteType.INSERT -> BEFORE_INSERT
                WriteType.UPDATE -> BEFORE_UPDATE
                WriteType.DELETE -> BEFORE_DELETE
            }
            callback = { triggerApi: String, trigger: () -> Unit ->
                val flag = "$objectApiName:$triggerApi"
                if (stack.count { it == flag } == 6) {
                    logger.info("当前触发器调用栈:{}", stack)
                    throw InnerException("可能存在递归调用，终止执行触发器")
                }
                stack.push(flag)
                try {
                    trigger.invoke()
                } finally {
                    stack.pop()
                }
            }
            scriptService.executeTrigger(
                dataService = wrapper,
                event = event,
                objectId = bizObjectId,
                old = old,
                new = new,
                tmpMap = tmpMap,
                around = callback,
            )
        }
        val (sql, params) = beforeExecutingSql(old, new)
        var result: MutableMap<String, Any?>? = database.useConnection {
            it.execute(sql, params).firstOrNull()
        }
        val pair = afterExecutingSql(result)
        result = pair.first
        new = pair.second?.let { Collections.unmodifiableMap(it) }

        if (withTrigger) {
            val event = when (type) {
                WriteType.INSERT -> AFTER_INSERT
                WriteType.UPDATE -> AFTER_UPDATE
                WriteType.DELETE -> AFTER_DELETE
            }
            scriptService.executeTrigger(
                dataService = wrapper,
                event = event,
                objectId = bizObjectId,
                old = old,
                new = new,
                tmpMap = tmpMap,
                around = requireNotNull(callback),
            )
        }
        return result
    }


    override fun getObjectByApi(objectApiName: String): ObjectInfo? {
        val apiKey = "getObjectByApi:$objectApiName"
        cache[apiKey]?.apply {
            return this as ObjectInfo
        }
        val bizObject = bizObjectService.findActiveObjectByApi(objectApiName) ?: return null
        val info = bizObject.run {
            ObjectInfo(
                id = id,
                name = name,
                api = this.api,
                type = type,
                moduleApi = moduleApi,
                canInsert = canInsert,
                canUpdate = canUpdate,
                canDelete = canDelete,
                canSoftDelete = canSoftDelete
            )
        }
        val idKey = "getObjectById:${info.id}"
        cache[apiKey] = info
        cache[idKey] = info
        return info
    }

    override fun getObjectById(id: String): ObjectInfo? {
        val idKey = "getObjectById:$id"
        cache[idKey]?.apply {
            return this as ObjectInfo
        }
        val bizObject = bizObjectService.findActiveObjectById(id) ?: return null
        val info = bizObject.run {
            ObjectInfo(
                id = this.id,
                name = name,
                api = this.api,
                type = type,
                moduleApi = moduleApi,
                canInsert = canInsert,
                canUpdate = canUpdate,
                canDelete = canDelete,
                canSoftDelete = canSoftDelete
            )
        }
        val apiKey = "getObjectByApi:${info.api}"
        cache[idKey] = info
        cache[apiKey] = info
        return info
    }

    override fun getObjectPerm(objectApiName: String, roleId: String): ObjectPerm? {
        val key = "getObjectPerm:$roleId:$objectApiName"
        cache[key]?.apply {
            return this as ObjectPerm
        }
        val bizObject =
            objectPermService.findByRoleIdAndBizObjectId(roleId, objectApiName) ?: return null
        val perm = bizObject.run {
            ObjectPerm(
                roleId = this.roleId,
                bizObjectId = bizObjectId,
                ins = ins,
                upd = upd,
                del = del,
                que = que,
                queryWithAccountTree = queryWithAccountTree,
                queryWithOrg = queryWithOrg,
                queryWithOrgTree = queryWithOrgTree,
                queryAll = queryAll,
                editAll = editAll
            )
        }
        cache[key] = perm
        return perm
    }

    override fun getFieldApiMap(objectApiName: String): Map<String, FieldInfo> {
        val key = "getObjectPerm:$objectApiName"
        cache[key]?.apply {
            @Suppress("UNCHECKED_CAST")
            return this as Map<String, FieldInfo>
        }
        val objectInfo = getObjectByApi(objectApiName)
            ?: throw DataNotFoundException("对象 [api:$objectApiName]")

        val fieldList =
            fieldService.findByBizObjectId(objectInfo.id)
        var result = mutableMapOf<String, FieldInfo>()
        for (bizObjectField in fieldList) {
            result[bizObjectField.api] = bizObjectField.run {
                FieldInfo(
                    id = id,
                    objectId = bizObjectId,
                    name = name,
                    api = api,
                    type = type,
                    maxLength = maxLength,
                    minLength = minLength,
                    dictId = dictId,
                    optionalNumber = optionalNumber,
                    refId = refId,
                    refDeletePolicy = refDeletePolicy,
                    sId = sId,
                    sFieldId = sFieldId,
                    sType = sType,
                    standardType = standardType,
                    predefined = predefined
                )
            }
        }
        result = Collections.unmodifiableMap(result)
        cache[key] = result
        return result
    }

    override fun getFieldByRefObjectId(objectId: String): List<FieldInfo> {
        val key = "getFieldByRefObjectId:$objectId"
        cache[key]?.apply {
            @Suppress("UNCHECKED_CAST")
            return this as List<FieldInfo>
        }
        val list = fieldService.findReferenceFieldByRefId(objectId).map {
            FieldInfo(
                id = it.id,
                objectId = it.bizObjectId,
                name = it.name,
                api = it.api,
                type = it.type,
                maxLength = it.maxLength,
                minLength = it.minLength,
                dictId = it.dictId,
                optionalNumber = it.optionalNumber,
                refId = it.refId,
                refDeletePolicy = it.refDeletePolicy,
                sId = it.sId,
                sFieldId = it.sFieldId,
                sType = it.sType,
                standardType = it.standardType,
                predefined = it.predefined
            )
        }
        cache[key] = list
        return list
    }

    override fun getFieldApiSetWithPerm(
        objectApiName: String,
        roleId: String,
        read: Boolean,
        edit: Boolean
    ): Set<String> {
        val key = "getFieldApiSetWithPerm:$roleId:$objectApiName:$read:$edit"
        cache[key]?.apply {
            @Suppress("UNCHECKED_CAST")
            return this as Set<String>
        }
        val objectInfo = getObjectByApi(objectApiName)
            ?: throw DataNotFoundException("对象 [api:$objectApiName]")
        val fieldApiMap = getFieldApiMap(objectApiName)
        val fieldPerm = fieldPermCache.getOrPut(key) {
            fieldPermService.findViewByRoleIdAndBizObjectId(roleId, objectInfo.id)
                .map { FieldPerm(it.roleId, it.fieldId, it.pRead, it.pEdit) }
        }
        val idSet = fieldPerm.mapNotNull {
            if (edit) {
                if (it.pEdit) it.fieldId else null
            } else if (read) {
                if (it.pRead) it.fieldId else null
            } else {
                if (!it.pRead && !it.pEdit) it.fieldId else null
            }
        }.toSet()
        var res =
            fieldApiMap.values.mapNotNull { if (idSet.contains(it.id)) it.api else null }
                .toSet()
        res = Collections.unmodifiableSet(res)
        cache[key] = res
        return res
    }

    override fun getTypeList(objectApiName: String): Set<TypeInfo> {
        val key = "getTypeList:$objectApiName"
        cache[key]?.apply {
            @Suppress("UNCHECKED_CAST")
            return this as Set<TypeInfo>
        }
        val objectInfo = getObjectByApi(objectApiName)
            ?: throw DataNotFoundException("对象 [api:$objectApiName]")

        var result = typeService.findAvailableTypeByBizObjectId(objectInfo.id).map {
            TypeInfo(it.id, it.name)
        }.toSet()
        result = Collections.unmodifiableSet(result)
        cache[key] = result
        return result
    }

    override fun getVisibleTypeIdSet(objectApiName: String, roleId: String): Set<String> {
        val key = "getVisibleTypeIdSet:$roleId:$objectApiName"
        cache[key]?.apply {
            @Suppress("UNCHECKED_CAST")
            return this as Set<String>
        }
        val objectInfo = getObjectByApi(objectApiName)
            ?: throw DataNotFoundException("对象 [api:$objectApiName]")

        var result =
            typePermService.findByRoleIdAndBizObjectId(roleId, objectInfo.id)
                .mapNotNull { if (it.visible) it.typeId else null }.toSet()
        result = Collections.unmodifiableSet(result)
        cache[key] = result
        return result
    }

    override fun hasDataPerm(
        accountId: String,
        objectId: String,
        dataId: String,
        read: Boolean,
        update: Boolean,
        delete: Boolean,
        share: Boolean,
        owner: Boolean,
    ): Boolean {
        if (!read && !update && !share && !owner) return false
        val account =
            accountService.findById(accountId) ?: return false
        val bizObject = bizObjectService.findActiveObjectById(objectId) ?: return false
        val bizObjectPerm =
            objectPermService.findByRoleIdAndBizObjectId(account.roleId, objectId)
                ?: return false
//        要求查询权限但没有对象的查询权限或要求更新权限但没有对象的更新权限时直接返回false
        if ((read && !bizObjectPerm.que)
            || (update && !bizObjectPerm.upd)
            || (delete && !bizObjectPerm.del)
        ) {
            return false
        }

        val data = queryByIdWithPerm(bizObject.api, dataId) ?: return false
        if (data["owner_id"] != accountId) {
            if (owner) return false
            if (!account.admin && share) {
                return false
            }
            if (!bizObjectPerm.editAll && delete) {
                return false
            }
        }
        val shareTableList =
            database.useConnection {
                //language=PostgreSQL
                val sql = """
                    select * from hymn_view."${bizObject.api}_share" 
                    where data_id = ? and (role_id = ? or account_id = ? or org_id = ?)
                """
                it.execute(sql, dataId, account.roleId, account.id, account.orgId)
                    .map { ShareTable(it) }
            }
        if (update && shareTableList.all { it.readOnly }) {
            return false
        }
        return true
    }


    private fun processingOssValueChange(
        objectApiName: String,
        oldData: MutableMap<String, Any?>,
        newData: MutableMap<String, Any?>?
    ) {
        TODO()
    }
}