package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.BizObjectFieldPermDto
import github.afezeria.hymn.core.module.entity.BizObjectFieldPerm

/**
 * @author afezeria
 */
internal interface BizObjectFieldPermService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectFieldPermDto): Int

    fun create(dto: BizObjectFieldPermDto): String

    fun findAll(): MutableList<BizObjectFieldPerm>

    fun findById(id: String): BizObjectFieldPerm?

    fun findByIds(ids: List<String>): MutableList<BizObjectFieldPerm>

    fun findByRoleIdAndFieldId(
        roleId: String,
        fieldId: String,
    ): BizObjectFieldPerm?

    fun findByRoleIdAndFieldIdList(
        roleId: String,
        fieldIdList: Collection<String>,
    ): MutableList<BizObjectFieldPerm>

    fun findByRoleId(
        roleId: String,
    ): MutableList<BizObjectFieldPerm>

    fun findByFieldId(
        fieldId: String,
    ): MutableList<BizObjectFieldPerm>

    fun batchCreate(dtoList: List<BizObjectFieldPermDto>): Int

    fun batchSave(dtoList: List<BizObjectFieldPermDto>): Int
    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectFieldPerm>

    fun findByRoleIdAndBizObjectId(roleId: String, bizObjectId: String): List<BizObjectFieldPerm>

}