package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.Dict
import github.afezeria.hymn.core.module.dto.DictDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface DictService {

    fun removeById(id: String): Int

    fun update(id: String, dto: DictDto): Int

    fun create(dto: DictDto): String

    fun findAll(): List<Dict>

    fun findById(id: String): Dict?

    fun findByApi(
        api: String,
    ): Dict?


}