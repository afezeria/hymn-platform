package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.BizObjectDto
import github.afezeria.hymn.core.module.entity.BizObject

/**
 * @author afezeria
 */
interface BizObjectService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectDto): Int

    fun create(dto: BizObjectDto): String

    fun findAll(): MutableList<BizObject>

    fun findById(id: String): BizObject?

    fun findByIds(ids: List<String>): MutableList<BizObject>

    fun findByApi(
        api: String,
    ): BizObject?

    /**
     * 停用对象
     * @param id 对象id
     */
    fun inactivateObjectById(id: String): Int

    /**
     * 启用对象
     */
    fun activateObjectById(id: String): Int

}