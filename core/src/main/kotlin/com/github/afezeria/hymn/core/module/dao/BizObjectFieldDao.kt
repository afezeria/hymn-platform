package com.github.afezeria.hymn.core.module.dao

import com.github.afezeria.hymn.common.db.AbstractDao
import com.github.afezeria.hymn.common.platform.DatabaseService
import com.github.afezeria.hymn.core.module.entity.BizObjectField
import com.github.afezeria.hymn.core.module.table.CoreBizObjectFields
import com.github.afezeria.hymn.core.module.table.CoreBizObjects
import org.ktorm.dsl.*
import org.ktorm.schema.ColumnDeclaring
import org.springframework.stereotype.Component

/**
 * @author afezeria
 */
@Component
class BizObjectFieldDao(
    databaseService: DatabaseService
) : AbstractDao<BizObjectField, CoreBizObjectFields>(
    table = CoreBizObjectFields(),
    databaseService = databaseService
) {
    val bizObjects = CoreBizObjects()

    fun selectByBizObjectIdAndApi(
        bizObjectId: String,
        api: String,
    ): BizObjectField? {
        return singleRowSelect(listOf(table.bizObjectId eq bizObjectId, table.api eq api))
    }

    fun selectByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectField> {
        return select({ it.bizObjectId eq bizObjectId })
    }

    fun selectByRefIdAndActiveTrue(refObjectId: String): MutableList<BizObjectField> {
        return databaseService.db().from(table)
            .leftJoin(bizObjects, bizObjects.id eq table.refId)
            .select(table.columns)
            //对象停用时不能有关联到该对象的启用的字段，所以这里不需要判断被关联对象的启用状态
            .where { (table.refId eq refObjectId) and (table.active eq true) }
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectActiveField(
        whereExpr: ((CoreBizObjectFields) -> ColumnDeclaring<Boolean>)
    ): MutableList<BizObjectField> {
        return databaseService.db().from(table)
            .leftJoin(bizObjects, bizObjects.id eq table.bizObjectId)
            .select(table.columns)
            .where { (bizObjects.active eq true) and (table.active eq true) and whereExpr(table) }
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectUpdatableField(
        whereExpr: ((CoreBizObjectFields) -> ColumnDeclaring<Boolean>)
    ): MutableList<BizObjectField> {
        return databaseService.db().from(table)
            .leftJoin(bizObjects, bizObjects.id eq table.bizObjectId)
            .select(table.columns)
            .where {
                (bizObjects.active eq true) and
                    (table.active eq true) and
                    ((table.predefined eq false) or (table.standardType eq "name")) and
                    whereExpr(table)
            }
            .mapTo(ArrayList()) { table.createEntity(it) }
    }

    fun selectCanModifyActiveStateFieldByIdAndActive(id: String, active: Boolean): BizObjectField? {
        return databaseService.db().from(table)
            .leftJoin(bizObjects, bizObjects.id eq table.bizObjectId)
            .select(table.columns)
            .where {
                (bizObjects.active eq true) and
                    (table.predefined eq false) and
                    (table.active eq active) and
                    (table.id eq id)
            }
            .mapTo(ArrayList()) { table.createEntity(it) }
            .firstOrNull()
    }
}