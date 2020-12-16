package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.BusinessCodeRef
import github.afezeria.hymn.core.module.dto.BusinessCodeRefDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface BusinessCodeRefService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BusinessCodeRefDto): Int

    fun create(dto: BusinessCodeRefDto): String

    fun findAll(): List<BusinessCodeRef>

    fun findById(id: String): BusinessCodeRef?

    fun findByFieldId(
        fieldId: String,
    ): List<BusinessCodeRef>

    fun findByOrgId(
        orgId: String,
    ): List<BusinessCodeRef>

    fun findByRoleId(
        roleId: String,
    ): List<BusinessCodeRef>


}