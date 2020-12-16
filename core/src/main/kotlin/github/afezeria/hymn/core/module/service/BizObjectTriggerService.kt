package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.BizObjectTrigger
import github.afezeria.hymn.core.module.dto.BizObjectTriggerDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface BizObjectTriggerService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectTriggerDto): Int

    fun create(dto: BizObjectTriggerDto): String

    fun findAll(): List<BizObjectTrigger>

    fun findById(id: String): BizObjectTrigger?

    fun findByBizObjectIdAndApi(
        bizObjectId: String,
        api: String,
    ): BizObjectTrigger?


}