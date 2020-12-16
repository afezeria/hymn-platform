package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.CustomInterface
import github.afezeria.hymn.core.module.dto.CustomInterfaceDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface CustomInterfaceService {

    fun removeById(id: String): Int

    fun update(id: String, dto: CustomInterfaceDto): Int

    fun create(dto: CustomInterfaceDto): String

    fun findAll(): List<CustomInterface>

    fun findById(id: String): CustomInterface?

    fun findByApi(
        api: String,
    ): CustomInterface?


}