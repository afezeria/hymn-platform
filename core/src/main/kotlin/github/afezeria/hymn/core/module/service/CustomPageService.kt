package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.CustomPage
import github.afezeria.hymn.core.module.dto.CustomPageDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface CustomPageService {

    fun removeById(id: String): Int

    fun update(id: String, dto: CustomPageDto): Int

    fun create(dto: CustomPageDto): String

    fun findAll(): MutableList<CustomPage>

    fun findById(id: String): CustomPage?

    fun findByIds(ids: List<String>): MutableList<CustomPage>

    fun findByApi(
        api: String,
    ): CustomPage?


}