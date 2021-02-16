package github.afezeria.hymn.core.module.dao

import github.afezeria.hymn.common.db.AbstractDao
import github.afezeria.hymn.common.platform.DatabaseService
import github.afezeria.hymn.core.module.entity.BusinessCodeRef
import github.afezeria.hymn.core.module.table.CoreBusinessCodeRefs
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BusinessCodeRefDao(
    databaseService: DatabaseService
) : AbstractDao<BusinessCodeRef, CoreBusinessCodeRefs>(
    table = CoreBusinessCodeRefs(),
    databaseService = databaseService
) {


    fun selectByFieldId(
        fieldId: String,
    ): MutableList<BusinessCodeRef> {
        return select({ it.fieldId eq fieldId })
    }

    fun selectByOrgId(
        orgId: String,
    ): MutableList<BusinessCodeRef> {
        return select({ it.orgId eq orgId })
    }

    fun selectByRoleId(
        roleId: String,
    ): MutableList<BusinessCodeRef> {
        return select({ it.roleId eq roleId })
    }


}