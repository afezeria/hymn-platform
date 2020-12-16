package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.BizObjectTypeOptions
import github.afezeria.hymn.core.module.dto.BizObjectTypeOptionsDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface BizObjectTypeOptionsService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectTypeOptionsDto): Int

    fun create(dto: BizObjectTypeOptionsDto): String

    fun findAll(): List<BizObjectTypeOptions>

    fun findById(id: String): BizObjectTypeOptions?

    fun findByBizObjectId(
        bizObjectId: String,
    ): List<BizObjectTypeOptions>

    fun findByTypeId(
        typeId: String,
    ): List<BizObjectTypeOptions>


}