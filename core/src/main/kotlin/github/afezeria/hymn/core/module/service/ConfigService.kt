package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.Config
import github.afezeria.hymn.core.module.dto.ConfigDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface ConfigService {

    fun removeById(id: String): Int

    fun update(id: String, dto: ConfigDto): Int

    fun create(dto: ConfigDto): String

    fun findAll(): List<Config>

    fun findById(id: String): Config?

    fun findByKey(
        key: String,
    ): List<Config>


}