package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.BizObjectTypeLayoutDto
import github.afezeria.hymn.core.module.entity.BizObjectTypeLayout

/**
 * @author afezeria
 */
interface BizObjectTypeLayoutService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectTypeLayoutDto): Int

    fun create(dto: BizObjectTypeLayoutDto): String

    fun findAll(): MutableList<BizObjectTypeLayout>

    fun findById(id: String): BizObjectTypeLayout?

    fun findByIds(ids: List<String>): MutableList<BizObjectTypeLayout>

    fun findByRoleIdAndTypeIdAndLayoutId(
        roleId: String,
        typeId: String,
        layoutId: String,
    ): BizObjectTypeLayout?

    fun findByRoleId(
        roleId: String,
    ): MutableList<BizObjectTypeLayout>

    fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectTypeLayout>

    fun batchCreate(dtoList: List<BizObjectTypeLayoutDto>): MutableList<Int>


}