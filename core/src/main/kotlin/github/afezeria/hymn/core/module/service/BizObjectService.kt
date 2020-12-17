package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.BizObject
import github.afezeria.hymn.core.module.dto.BizObjectDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

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


}