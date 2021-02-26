package github.afezeria.hymn.core.service.dataservice

import github.afezeria.hymn.common.constant.AccountType
import github.afezeria.hymn.common.exception.PermissionDeniedException
import github.afezeria.hymn.common.platform.Session
import github.afezeria.hymn.common.util.execute

/**
 * @author afezeria
 */
interface ScriptDataServiceForShare : ScriptDataServiceForQuery {
    override fun share(
        objectApiName: String,
        dataId: String,
        accountId: String?,
        roleId: String?,
        orgId: String?,
        readOnly: Boolean
    ): Boolean {
        return share(objectApiName, dataId, accountId, roleId, orgId, readOnly, false)
    }

    override fun shareWithPerm(
        objectApiName: String,
        dataId: String,
        accountId: String?,
        roleId: String?,
        orgId: String?,
        readOnly: Boolean
    ): Boolean {
        return share(objectApiName, dataId, accountId, roleId, orgId, readOnly, true)
    }

    private fun share(
        objectApiName: String,
        dataId: String,
        accountId: String?,
        roleId: String?,
        orgId: String?,
        readOnly: Boolean,
        withPerm: Boolean,
    ): Boolean {
        if ((accountId == null || accountId.isBlank())
            && (roleId == null || roleId.isBlank())
            && (orgId == null || orgId.isBlank())
        ) {
            return false
        }

        val data = queryByIdWithPerm(objectApiName, dataId) ?: return false
        if (withPerm) {
            val session = Session.getInstance()
            if (data["owner_id"] != session.accountId && session.accountType != AccountType.ADMIN) {
                throw PermissionDeniedException("当前用户不是数据所有人，缺少共享权限")
            }
        }
        accountId?.apply {
            val account = queryById("account", this)
            if (account == null) {
                logger.info("共享失败，用户[id:{}]不存在", this)
                return false
            }
        }
        roleId?.apply {
            val role = queryById("role", this)
            if (role == null) {
                logger.info("共享失败，角色[id:{}]不存在", this)
                return false
            }
        }
        orgId?.apply {
            val org = queryById("org", this)
            if (org == null) {
                logger.info("共享失败，组织[id:{}]不存在", this)
                return false
            }
        }
        val objectInfo = getObjectByApi(objectApiName)!!
        database.useConnection {
            val sql = """
                insert into hymn_view."${objectInfo.api}_share" (data_id,role_id,org_id,account_id,read_only)
                values (?,?,?,?,?);
            """.trimIndent()
            it.execute(sql, dataId, roleId, orgId, accountId, readOnly)
        }
        return true
    }
}