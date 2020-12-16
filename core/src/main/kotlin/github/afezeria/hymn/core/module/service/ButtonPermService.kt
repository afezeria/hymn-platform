package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.ButtonPerm
import github.afezeria.hymn.core.module.dto.ButtonPermDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface ButtonPermService {

    fun removeById(id: String): Int

    fun update(id: String, dto: ButtonPermDto): Int

    fun create(dto: ButtonPermDto): String

    fun findAll(): List<ButtonPerm>

    fun findById(id: String): ButtonPerm?

    fun findByRoleIdAndButtonId(
        roleId: String,
        buttonId: String,
    ): ButtonPerm?

    fun findByRoleId(
        roleId: String,
    ): List<ButtonPerm>

    fun findByButtonId(
        buttonId: String,
    ): List<ButtonPerm>


}