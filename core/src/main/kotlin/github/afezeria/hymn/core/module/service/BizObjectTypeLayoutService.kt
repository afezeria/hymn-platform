package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.BizObjectTypeLayout
import github.afezeria.hymn.core.module.dto.BizObjectTypeLayoutDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface BizObjectTypeLayoutService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectTypeLayoutDto): Int

    fun create(dto: BizObjectTypeLayoutDto): String

    fun findAll(): List<BizObjectTypeLayout>

    fun findById(id: String): BizObjectTypeLayout?

    fun findByRoleIdAndTypeIdAndLayoutId(
        roleId: String,
        typeId: String,
        layoutId: String,
    ): BizObjectTypeLayout?

    fun findByRoleId(
        roleId: String,
    ): List<BizObjectTypeLayout>

    fun findByBizObjectId(
        bizObjectId: String,
    ): List<BizObjectTypeLayout>


}