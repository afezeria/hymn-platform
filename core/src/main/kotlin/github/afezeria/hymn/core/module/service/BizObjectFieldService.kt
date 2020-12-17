package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.BizObjectField
import github.afezeria.hymn.core.module.dto.BizObjectFieldDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface BizObjectFieldService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectFieldDto): Int

    fun create(dto: BizObjectFieldDto): String

    fun findAll(): MutableList<BizObjectField>

    fun findById(id: String): BizObjectField?

    fun findByIds(ids: List<String>): MutableList<BizObjectField>

    fun findByBizObjectIdAndApi(
        bizObjectId: String,
        api: String,
    ): BizObjectField?

    fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectField>


}