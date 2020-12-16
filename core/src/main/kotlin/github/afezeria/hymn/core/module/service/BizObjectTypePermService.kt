package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.BizObjectTypePerm
import github.afezeria.hymn.core.module.dto.BizObjectTypePermDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface BizObjectTypePermService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectTypePermDto): Int

    fun create(dto: BizObjectTypePermDto): String

    fun findAll(): List<BizObjectTypePerm>

    fun findById(id: String): BizObjectTypePerm?

    fun findByRoleIdAndTypeId(
        roleId: String,
        typeId: String,
    ): BizObjectTypePerm?

    fun findByTypeId(
        typeId: String,
    ): List<BizObjectTypePerm>


}