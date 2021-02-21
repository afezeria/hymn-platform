package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.BusinessCodeRefDto
import github.afezeria.hymn.core.module.entity.BusinessCodeRef

/**
 * @author afezeria
 */
interface BusinessCodeRefService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BusinessCodeRefDto): Int

    fun create(dto: BusinessCodeRefDto): String

    fun findAll(): MutableList<BusinessCodeRef>

    fun findById(id: String): BusinessCodeRef?

    fun findByIds(ids: List<String>): MutableList<BusinessCodeRef>

    fun findByFieldId(
        fieldId: String,
    ): MutableList<BusinessCodeRef>

    fun findByOrgId(
        orgId: String,
    ): MutableList<BusinessCodeRef>

    fun findByRoleId(
        roleId: String,
    ): MutableList<BusinessCodeRef>

    fun pageFind(pageSize: Int, pageNum: Int): List<BusinessCodeRef>


}