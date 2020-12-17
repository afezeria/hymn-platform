package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.Org
import github.afezeria.hymn.core.module.dto.OrgDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface OrgService {

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