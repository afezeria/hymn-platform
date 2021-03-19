package com.github.afezeria.hymn.core.platform.dataservice

import com.github.afezeria.hymn.common.constant.AccountType
import com.github.afezeria.hymn.common.exception.PermissionDeniedException
import com.github.afezeria.hymn.common.platform.Session
import com.github.afezeria.hymn.common.platform.dataservice.ShareTable
import com.github.afezeria.hymn.common.util.execute

/**
 * @author afezeria
 */
interface ScriptDataServiceForShare : ScriptDataService {
    override fun getShare(
        objectApiName: String,
        ids: Collection<String>,
        roleId: String?,
        orgId: String?,
        accountId: String?,
        readOnly: Boolean?,
    ): MutableList<ShareTable> {
        getObjectByApi(objectApiName) ?: return mutableListOf()
        val params = mutableListOf<Any?>()
        params.add(ids)
        val subWhere = listOf(
            "role_id" to roleId,
            "account_id" to accountId,
            "org_id" to orgId
        ).filter { it.second != null }
            .run {
                for (pair in this) {
                    params.add(pair.second)
                }
                joinToString(" or ", " and (", ") ") { "${it.first} = ?" }
            }
        val readOnlySubWhere = if (readOnly != null) {
            params.add(readOnly)
            " and read_only = ?"
        } else {
            ""
        }
        //language=PostgreSQL
        val sql = """
            select data_id,role_id,org_id,account_id,read_only from hymn_view."${objectApiName}_share" 
            where data_id = any (?) $subWhere $readOnlySubWhere
        """.trimIndent()
        database.useConnection {
            return it.execute(sql, params)
                .mapTo(ArrayList()) { ShareTable(it) }
        }
    }

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