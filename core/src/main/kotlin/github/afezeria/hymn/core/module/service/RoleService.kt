package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.Role
import github.afezeria.hymn.core.module.dto.RoleDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface RoleService {

    fun removeById(id: String): Int

    fun update(id: String, dto: RoleDto): Int

    fun create(dto: RoleDto): String

    fun findAll(): List<Role>

    fun findById(id: String): Role?


}