package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.BizObjectPermDto
import github.afezeria.hymn.core.module.entity.BizObjectPerm

/**
 * @author afezeria
 */
internal interface BizObjectPermService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectPermDto): Int

    fun create(dto: BizObjectPermDto): String

    fun batchCreate(dtoList: List<BizObjectPermDto>): Int

    /**
     * insert or update on conflict (roleId,bizObjectId)
     */
    fun batchSave(dtoList: List<BizObjectPermDto>): Int

    fun findAll(): MutableList<BizObjectPerm>

    fun findById(id: String): BizObjectPerm?

    fun findByIds(ids: List<String>): MutableList<BizObjectPerm>

    fun findByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String,
    ): BizObjectPerm?

    fun findByRoleId(
        roleId: String,
    ): MutableList<BizObjectPerm>

    fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectPerm>

    fun pageFind(pageSize: Int, pageNum: Int): List<BizObjectPerm>


}