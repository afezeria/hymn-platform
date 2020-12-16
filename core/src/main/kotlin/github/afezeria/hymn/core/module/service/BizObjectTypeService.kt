package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.BizObjectType
import github.afezeria.hymn.core.module.dto.BizObjectTypeDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface BizObjectTypeService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectTypeDto): Int

    fun create(dto: BizObjectTypeDto): String

    fun findAll(): List<BizObjectType>

    fun findById(id: String): BizObjectType?

    fun findByBizObjectIdAndName(
        bizObjectId: String,
        name: String,
    ): BizObjectType?


}