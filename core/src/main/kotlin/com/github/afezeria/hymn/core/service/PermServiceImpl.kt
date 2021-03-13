package com.github.afezeria.hymn.core.service

import com.github.afezeria.hymn.common.platform.PermService
import com.github.afezeria.hymn.common.platform.dataservice.DataService
import com.github.afezeria.hymn.core.module.service.BizObjectFieldPermService
import com.github.afezeria.hymn.core.module.service.BizObjectPermService
import com.github.afezeria.hymn.core.module.service.ModuleFunctionPermService
import org.springframework.beans.factory.annotation.Autowired

/**
 * @author afezeria
 */
class PermServiceImpl : PermService {
    @Autowired
    private lateinit var dataService: DataService

    @Autowired
    private lateinit var functionPermService: ModuleFunctionPermService

    @Autowired
    private lateinit var objectPermService: BizObjectPermService

    @Autowired
    private lateinit var fieldPermService: BizObjectFieldPermService

    override fun hasFunctionPerm(roleId: String, name: String): Boolean {
        return functionPermService.findByRoleIdAndFunctionApi(roleId, name)?.perm ?: false
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
        return dataService.hasDataPerm(
            accountId,
            objectId,
            dataId,
            read,
            update,
            delete,
            share,
            owner
        )
    }

    override fun hasObjectPerm(
        roleId: String,
        objectId: String,
        query: Boolean?,
        create: Boolean?,
        update: Boolean?,
        delete: Boolean?
    ): Boolean {
        val perm =
            objectPermService.findByRoleIdAndBizObjectId(roleId, objectId) ?: return false
        if (query != null && perm.que != query) {
            return false
        }
        if (create != null && perm.ins != create) {
            return false
        }
        if (update != null && perm.upd != update) {
            return false
        }
        if (delete != null && perm.del != delete) {
            return false
        }
        return true
    }

    override fun hasFieldPerm(
        roleId: String,
        fieldId: String,
        read: Boolean?,
        edit: Boolean?
    ): Boolean {
        val perm =
            fieldPermService.findByRoleIdAndFieldId(roleId, fieldId) ?: return false
        if (read != null && perm.pRead != read) {
            return false
        }
        if (edit != null && perm.pEdit != edit) {
            return false
        }
        return true
    }
}