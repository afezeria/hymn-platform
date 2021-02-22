package github.afezeria.hymn.core.service.dataservice

import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.common.platform.dataservice.FieldInfo
import github.afezeria.hymn.common.platform.dataservice.ObjectInfo
import github.afezeria.hymn.common.platform.dataservice.ObjectPerm
import github.afezeria.hymn.common.platform.dataservice.TypeInfo
import github.afezeria.hymn.core.module.service.*
import mu.KLoggable
import mu.KLogger
import org.ktorm.database.Database

/**
 * @author afezeria
 */
class ScriptDataServiceImpl(
    private val bizObjectService: BizObjectService,
    private val typeService: BizObjectTypeService,
    private val fieldService: BizObjectFieldService,
    private val objectPermService: BizObjectPermService,
    private val typePermService: BizObjectTypePermService,
    private val fieldPermService: BizObjectFieldPermService,
    private val databaseService: DatabaseService,
) : ScriptDataService, ScriptDataServiceForQuery,
    ScriptDataServiceForInsert, ScriptDataServiceForUpdate, ScriptDataServiceForDelete, KLoggable {


    override val logger: KLogger = logger()

    override val database: Database
        get() = TODO("Not yet implemented")

    override fun execute(
        sql: String,
        params: Collection<Any?>,
        type: WriteType,
        oldData: MutableMap<String, Any?>?,
        newData: MutableMap<String, Any?>?,
        trigger: Boolean
    ): MutableMap<String, Any?> {
        TODO("Not yet implemented")
    }

    override fun getObject(api: String): ObjectInfo? {
        TODO("Not yet implemented")
    }

    override fun getObjectPerm(roleId: String, objectApiName: String): ObjectPerm? {
        TODO("Not yet implemented")
    }

    override fun getFieldMap(objectApiName: String): Map<String, FieldInfo> {
        TODO("Not yet implemented")
    }

    override fun getFieldApiSetWithPerm(
        roleId: String,
        objectApiName: String,
        read: Boolean?,
        edit: Boolean?
    ): Set<String> {
        TODO("Not yet implemented")
    }

    override fun getTypeList(objectApiName: String): Set<TypeInfo> {
        TODO("Not yet implemented")
    }

    override fun getVisibleTypeIdSet(roleId: String, objectApiName: String): Set<String> {
        TODO("Not yet implemented")
    }

    override fun hasDataPerm(
        accountId: String,
        objectId: String,
        dataId: String,
        read: Boolean?,
        update: Boolean?,
        share: Boolean?,
        owner: Boolean?
    ): Boolean {
        TODO("Not yet implemented")
    }

}