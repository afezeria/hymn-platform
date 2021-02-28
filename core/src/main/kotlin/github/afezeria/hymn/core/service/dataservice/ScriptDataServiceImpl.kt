package github.afezeria.hymn.core.service.dataservice

import github.afezeria.hymn.common.constant.TriggerEvent.*
import github.afezeria.hymn.common.exception.BusinessException
import github.afezeria.hymn.common.exception.DataNotFoundException
import github.afezeria.hymn.common.exception.InnerException
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.dataservice.*
import github.afezeria.hymn.common.platform.script.ScriptService
import github.afezeria.hymn.common.platform.script.TriggerInfo
import github.afezeria.hymn.common.util.execute
import github.afezeria.hymn.core.module.service.*
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
        sqlBuilder: (
            oldData: Map<String, Any?>?,
            newData: NewRecordMap?,
        ) -> Pair<String, Collection<Any?>>,
        type: WriteType,
        objectApiName: String,
        oldData: Map<String, Any?>?,
        newData: NewRecordMap?,
        withTrigger: Boolean,
    ): MutableMap<String, Any?> {
        var old: Map<String, Any?>? = oldData
        val new: NewRecordMap? = newData
        if (type == WriteType.DELETE || type == WriteType.UPDATE) {
            old = Collections.unmodifiableMap(old)
        }
        val result: MutableMap<String, Any?>?
        if (withTrigger) {
            var event = when (type) {
                WriteType.INSERT -> BEFORE_INSERT
                WriteType.UPDATE -> BEFORE_UPDATE
                WriteType.DELETE -> BEFORE_DELETE
            }
            val callback = { info: TriggerInfo, trigger: () -> Unit ->
                val flag = "$objectApiName:${info.api}"
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
                objectApiName = objectApiName,
                old = old,
                new = new,
                tmpMap = tmpMap,
                around = callback,
            )
            val (sql, params) = sqlBuilder(old, new)
            database.useConnection {
                result = it.execute(sql, params).firstOrNull()
            }
            event = when (type) {
                WriteType.INSERT -> AFTER_INSERT
                WriteType.UPDATE -> AFTER_UPDATE
                WriteType.DELETE -> AFTER_DELETE
            }
            scriptService.executeTrigger(
                dataService = wrapper,
                event = event,
                objectApiName = objectApiName,
                old = old,
                new = new,
                tmpMap = tmpMap,
                around = callback,
            )
        } else {
            val (sql, params) = sqlBuilder(old, new)
            database.useConnection {
                result = it.execute(sql, params).firstOrNull()
            }
        }
//        主对象删除时没有触发触发器但关联对象的删除触发器还是会正常触发
        if (type == WriteType.DELETE) {
            processingDeletePolicy(objectApiName, requireNotNull(old))
        }
        if (result == null) {
            val str = when (type) {
                WriteType.INSERT -> "插入失败" //正常情况不会出现这个
                WriteType.UPDATE -> "更新失败，数据不存在"
                WriteType.DELETE -> "删除失败，数据不存在"
            }
            throw BusinessException(str)
        }
        return result
    }


    override fun getObjectByApi(api: String): ObjectInfo? {
        val apiKey = "getObjectByApi:$api"
        cache[apiKey]?.apply {
            return this as ObjectInfo
        }
        val bizObject = bizObjectService.findByApi(api) ?: return null
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
        val bizObject = bizObjectService.findById(id) ?: return null
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

    override fun getObjectPerm(roleId: String, objectApiName: String): ObjectPerm? {
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

    override fun getFieldApiSetWithPerm(
        roleId: String,
        objectApiName: String,
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
            fieldPermService.findByRoleIdAndBizObjectId(roleId, objectInfo.id)
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

        var result = typeService.findByBizObjectId(objectInfo.id).map {
            TypeInfo(it.id, it.name)
        }.toSet()
        result = Collections.unmodifiableSet(result)
        cache[key] = result
        return result
    }

    override fun getVisibleTypeIdSet(roleId: String, objectApiName: String): Set<String> {
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
        val bizObject = bizObjectService.findById(objectId) ?: return false
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

    /**
     * 处理删除时的级联和阻止动作
     */
    private fun processingDeletePolicy(objectApiName: String, oldData: Map<String, Any?>) {
        val objectInfo = requireNotNull(getObjectByApi(objectApiName))
        val masterDataId = requireNotNull(oldData["id"]) as String
        val refFieldList = fieldService.findReferenceFieldByRefId(objectInfo.id)
        if (refFieldList.isEmpty()) return
        val refFieldMap = refFieldList.groupBy { it.refDeletePolicy }
        refFieldMap["restrict"]?.let {
            for (bizObjectField in it) {
                val refObjectInfo = requireNotNull(getObjectById(bizObjectField.bizObjectId))
                val subDataList =
                    query(refObjectInfo.api, "\"${bizObjectField.api}\" = ?", listOf(masterDataId))
                if (subDataList.isNotEmpty()) {
                    throw BusinessException("删除失败，当前数据被${refObjectInfo.name}对象的数据引用，请删除所有引用数据后再执行删除操作")
                }
            }
        }
        refFieldMap["cascade"]?.let {
            for (bizObjectField in it) {
                val refObjectInfo = requireNotNull(getObjectById(bizObjectField.bizObjectId))
                val subDataList =
                    query(refObjectInfo.api, "\"${bizObjectField.api}\" = ?", listOf(masterDataId))
                for (subData in subDataList) {
                    delete(refObjectInfo.api, requireNotNull(subData["id"]) as String)
                }
            }
        }
        refFieldMap["set_null"]?.let {
            for (bizObjectField in it) {
                val refObjectInfo = requireNotNull(getObjectById(bizObjectField.bizObjectId))
                //language=PostgreSQL
                sql(
                    """
                    update hymn_view."${refObjectInfo.api}"
                    set "${bizObjectField.api}" = null
                    where "${bizObjectField.api}" = ?
                """, masterDataId
                )
            }
        }
    }

    private fun processingOssValueChange(
        objectApiName: String,
        oldData: MutableMap<String, Any?>,
        newData: MutableMap<String, Any?>?
    ) {
    }
}