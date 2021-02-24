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
) : ScriptDataService, ScriptDataServiceForQuery,
    ScriptDataServiceForInsert, ScriptDataServiceForUpdate, ScriptDataServiceForDelete, KLoggable {

    private val cache = HashMap<String, Any>()
    private val fieldPermCache = HashMap<String, List<FieldPerm>>()

    private val tmpMap = HashMap<String, Any?>()
    private val wrapper = DataServiceWrapper(this)

    private val stack = Stack<String>()


    override val logger: KLogger = logger()

    override val database: Database
        get() = databaseService.user()

    override fun execute(
        sql: String,
        params: Collection<Any?>,
        type: WriteType,
        objectApiName: String,
        oldData: MutableMap<String, Any?>?,
        newData: MutableMap<String, Any?>?,
        trigger: Boolean,
    ): MutableMap<String, Any?> {
        val result: MutableMap<String, Any?>?
        if (trigger) {
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
                old = oldData,
                new = newData,
                tmpMap = tmpMap,
                around = callback,
            )
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
                old = oldData,
                new = newData,
                tmpMap = tmpMap,
                around = callback,
            )
        } else {
            database.useConnection {
                result = it.execute(sql, params).firstOrNull()
            }
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

    override fun getObject(api: String): ObjectInfo? {
        val key = "getObject:$api"
        cache[key]?.apply {
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
        cache[key] = info
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
        val objectInfo = getObject(objectApiName)
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
        val objectInfo = getObject(objectApiName)
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
            fieldApiMap.values.mapNotNull { if (idSet.contains(it.id)) it.api else null }.toSet()
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
        val objectInfo = getObject(objectApiName)
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
        val objectInfo = getObject(objectApiName)
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
        share: Boolean,
        owner: Boolean,
    ): Boolean {
        if (!read && !update && !share && !owner) return false
        val account =
            accountService.findById(accountId) ?: return false
        val bizObject = bizObjectService.findById(objectId) ?: return false
        val bizObjectPerm =
            objectPermService.findByRoleIdAndBizObjectId(account.roleId, objectId) ?: return false
//        要求查询权限但没有对象的查询权限或要求更新权限但没有对象的更新权限时直接返回false
        if ((read && !bizObjectPerm.que)
            || (update && !bizObjectPerm.upd)
        ) {
            return false
        }
        val data = queryByIdWithPerm(bizObject.api, dataId) ?: return false
//        只要求读权限时如果查询出了数据说明可读，直接返回true
        if (read && !update) {
            return true
        }
        if (data["owner_id"] == accountId) {
//        数据所有者必定有共享和所有者权限
            if (owner || share) return true
        } else {

            if (owner) return false
            if (share) {
                return account.admin
            }
        }
//        queryWithPerm(
//            bizObject.api, "id = ?", listOf(dataId),
//            limit = 1,
//            fieldSet = setOf("id", "owner_id"),
//            writeable = false,
//            deletable = false,
//            shareable = false,
//        )
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
        if (update) {
            shareTableList.find { !it.readOnly }?.apply {
                return true
            }
        }
        return false
    }

}