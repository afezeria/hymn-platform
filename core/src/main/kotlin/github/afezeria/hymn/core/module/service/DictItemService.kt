package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.DictItem
import github.afezeria.hymn.core.module.dto.DictItemDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface DictItemService {

    fun removeById(id: String): Int

    fun update(id: String, dto: DictItemDto): Int

    fun create(dto: DictItemDto): String

    fun findAll(): MutableList<DictItem>

    fun findById(id: String): DictItem?

    fun findByIds(ids: List<String>): MutableList<DictItem>

    fun findByDictIdAndCode(
        dictId: String,
        code: String,
    ): DictItem?

    fun findByDictId(
        dictId: String,
    ): MutableList<DictItem>


}