package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.dto.OrgDto
import github.afezeria.hymn.core.module.entity.Org

/**
 * @author afezeria
 */
internal interface OrgService {

    fun removeById(id: String): Int

    fun update(id: String, dto: OrgDto): Int

    fun create(dto: OrgDto): String

    fun findAll(): MutableList<Org>

    fun findById(id: String): Org?

    fun findByIds(ids: List<String>): MutableList<Org>

    fun findByParentId(
        parentId: String,
    ): MutableList<Org>


}