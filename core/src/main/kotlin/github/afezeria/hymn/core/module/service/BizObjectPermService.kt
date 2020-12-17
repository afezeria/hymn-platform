package github.afezeria.hymn.core.module.service

import github.afezeria.hymn.core.module.entity.BizObjectPerm
import github.afezeria.hymn.core.module.dto.BizObjectPermDto
import github.afezeria.hymn.common.platform.DataBaseService
import github.afezeria.hymn.common.platform.SessionService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

/**
 * @author afezeria
 */
interface BizObjectPermService {

    fun removeById(id: String): Int

    fun update(id: String, dto: BizObjectPermDto): Int

    fun create(dto: BizObjectPermDto): String

    fun findAll(): MutableList<BizObjectPerm>

    fun findById(id: String): BizObjectPerm?

    fun findByIds(ids: List<String>): MutableList<BizObjectPerm>

    fun findByRoleIdAndBizObjectId(
        roleId: String,
        bizObjectId: String,
    ): BizObjectPerm?

    fun findByRoleId(
        roleId: String,
    ): MutableList<BizObjectPerm>

    fun findByBizObjectId(
        bizObjectId: String,
    ): MutableList<BizObjectPerm>


}