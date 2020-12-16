package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.SharedCode
import github.afezeria.hymn.core.module.dto.SharedCodeDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface SharedCodeService {

    fun removeById(id: String): Int

    fun update(id: String, dto: SharedCodeDto): Int

    fun create(dto: SharedCodeDto): String

    fun findAll(): List<SharedCode>

    fun findById(id: String): SharedCode?

    fun findByApi(
        api: String,
    ): SharedCode?


}